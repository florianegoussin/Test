package com.example.test.service;

//import com.example.test.model.LocationCoordinates;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.example.test.event.EventBusManager;
import com.example.test.event.MeasurementResultEvent;
import com.example.test.model.Measurement;
import com.example.test.model.MeasurementResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

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

        public  void searchMesures(final String location) {

            // Call to the REST service
            //Modification pour uniquement les locations en France
            mMesureSearchRESTService.searchForMesures("FR", location).enqueue(new Callback<MeasurementResult>() {
                @Override
                public void onResponse(Call<MeasurementResult> call, retrofit2.Response<MeasurementResult> response) {

                    System.out.println("aaaaaaaaaaaaa "+response.body().results);
                    // Post an event so that listening activities can update their UI
                    if (response.body() != null && response.body().results != null) {

                        ActiveAndroid.beginTransaction();
                        for (Measurement mesure : response.body().results) {

                            System.out.println("aaaaaaaaaaaaa "+mesure.location+"         "+mesure.city);

                            Measurement m = new Measurement();
                            m.location=mesure.location;
                            m.city=mesure.city;

                            List<Measurement.Values> value = new ArrayList<>();
                            for(Measurement.Values mv :mesure.measurements){
                                System.out.println("aaaaaaaaaaaaa "+mv.parameter+"       "+mv.value+"    "+mv.unit);

                                value.add(mv);
>>>>>>> cb33c7ecdcf50b135bf3212c7142e50b140a8359
                            }
                            m.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();


                     //   searchMesuresFromDB(search);

                    } else {
                        // Null result
                        // We may want to display a warning to user (e.g. Toast)
                    }
                }


                @Override
                public void onFailure(Call<MeasurementResult> call, Throwable t) {
                    // Request has failed or is not at expected format
                    // We may want to display a warning to user (e.g. Toast)
                    System.out.println("trooooooooooooooooooop triste pas de reponse");
                    Log.e("[PlaceSearcher] [REST]", "Response error : " + t.getMessage());
                    //searchLocationsFromDB(search);
                }

            });
        }

        public void searchMesuresFromDB (String search){
            List<Measurement> matchingMesureFromDB = new Select()
                    .from(Measurement.class)
                    .where("city LIKE '%" + search + "%'").orderBy("city ").execute();

            EventBusManager.BUS.post(new MeasurementResultEvent(matchingMesureFromDB));
        }

        // Service describing the REST APIs
        public interface MeasurementSearchRESTService {

            @GET("latest")
            Call<MeasurementResult> searchForMesures(@Query("country") String country, @Query("location") String location );

        }





}


