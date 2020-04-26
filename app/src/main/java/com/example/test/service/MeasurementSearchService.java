package com.example.test.service;

//import com.example.test.model.LocationCoordinates;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.example.test.event.EventBusManager;
import com.example.test.event.MeasurementResultEvent;
import com.example.test.model.Measurement;
import com.example.test.model.MeasurementValue;
import com.example.test.model.MesureSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MeasurementSearchService {

    private static final long REFRESH_DELAY = 650;
    public static MeasurementSearchService INSTANCE = new MeasurementSearchService();
    private final MeasurementSearchRESTService mMesureSearchRESTService;
    private ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture mLastScheduleTask;

    private MeasurementSearchService() {
        // Create GSON Converter that will be used to convert from JSON to Java
        Gson gsonConverter = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation().create();

        // Create Retrofit client
        Retrofit retrofit = new Retrofit.Builder()
                // Using OkHttp as HTTP Client
                .client(new OkHttpClient())
                // Having the following as server URL
                .baseUrl("https://api.openaq.org/v1/")
                // Using GSON to convert from Json to Java
                .addConverterFactory(GsonConverterFactory.create(gsonConverter))
                .build();

        // Use retrofit to generate our REST service code
        mMesureSearchRESTService = retrofit.create(MeasurementSearchRESTService.class);
    }

        public  void searchMesures(final String search,final String location) {

            if (mLastScheduleTask != null && !mLastScheduleTask.isDone()) {
                mLastScheduleTask.cancel(true);
            }


            // Schedule a network call in REFRESH_DELAY ms
            mLastScheduleTask = mScheduler.schedule(new Runnable() {
                public void run() {
                    searchMesuresFromDB(search);

                    // Call to the REST service
                    //Modification pour uniquement les locations en France
                    mMesureSearchRESTService.searchForMesures("FR",search,location).enqueue(new Callback<MesureSearchResult>() {
                        @Override
                        public void onResponse(Call<MesureSearchResult> call, retrofit2.Response<MesureSearchResult> response) {
                            // Post an event so that listening activities can update their UI
                            if (response.body() != null && response.body().mesures != null) {
                                EventBusManager.BUS.post(new MeasurementResultEvent(response.body().mesures));
                                ActiveAndroid.beginTransaction();
                                for (Measurement mesure : response.body().mesures) {
                                    Measurement m = new Measurement();
                                    m.location=mesure.location;
                                    m.city=mesure.city;
                                    m.country=mesure.country;
                                    List<MeasurementValue> value = new ArrayList<>();
                                    for(MeasurementValue mv :mesure.measurements){
                                        value.add(mv);
                                    }
                                    m.save();
                                }
                                ActiveAndroid.setTransactionSuccessful();
                                ActiveAndroid.endTransaction();


                                searchMesuresFromDB(search);

                            } else {
                                // Null result
                                // We may want to display a warning to user (e.g. Toast)
                            }
                        }


                        @Override
                        public void onFailure(Call<MesureSearchResult> call, Throwable t) {
                            // Request has failed or is not at expected format
                            // We may want to display a warning to user (e.g. Toast)
                            Log.e("[PlaceSearcher] [REST]", "Response error : " + t.getMessage());
                            //searchLocationsFromDB(search);
                        }

                    });
                }

            },REFRESH_DELAY, TimeUnit.MILLISECONDS);
        }

        public void searchMesuresFromDB (String search){
            List<Measurement> matchingMesureFromDB = new Select()
                    .from(Measurement.class)
                    .where("city LIKE '%" + search + "%'").orderBy("city ").execute();

            EventBusManager.BUS.post(new MeasurementResultEvent(matchingMesureFromDB));
        }

        // Service describing the REST APIs
        public interface MeasurementSearchRESTService {
            @GET("latest/")
            Call<MesureSearchResult> searchForMesures(@Query("country") String country, @Query("city") String city, @Query("location") String location );

        }





}


