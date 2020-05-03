package com.example.test.service;


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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.test.model.Measurement.*;

public class MeasurementSearchService {

    private static final long REFRESH_DELAY = 650;
    public static MeasurementSearchService INSTANCE = new MeasurementSearchService();
    private final MeasurementSearchRESTService mMesureSearchRESTService;
    private ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture mLastScheduleTask;

    private Gson gson ;

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

        gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
    }

        public  void searchMesures(final String location, final String city) {

            if (mLastScheduleTask != null && !mLastScheduleTask.isDone()) {
                mLastScheduleTask.cancel(true);
            }

            mLastScheduleTask = mScheduler.schedule(new Runnable() {

                public void run(){


            // Call to the REST service
            //Modification pour uniquement les locations en France
            mMesureSearchRESTService.searchForMesures("FR",city, location).enqueue(new Callback<MeasurementResult>() {
                @Override
                public void onResponse(Call<MeasurementResult> call, retrofit2.Response<MeasurementResult> response) {


                    // Post an event so that listening activities can update their UI
                    if (response.body() != null && response.body().results != null) {

                        ActiveAndroid.beginTransaction();
                        for (Measurement mesure : response.body().results) {

                            Measurement m = new Measurement();
                            m.location=mesure.location;
                            m.city=mesure.city;
                            List<Values> value = new ArrayList<>();

                            for(Values mv :mesure.measurements){
                                value.add(mv);
                            }

                            m.mesure= gson.toJson(value);
                            m.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();


                        searchMesuresFromDB(location);

                    } else {
                        // Null result
                        // We may want to display a warning to user (e.g. Toast)
                    }
                }


                @Override
                public void onFailure(Call<MeasurementResult> call, Throwable t) {
                    // Request has failed or is not at expected format
                    // We may want to display a warning to user (e.g. Toast)
                    Log.e("[PlaceSearcher] [REST]", "Response error : " + t.getMessage());
                    searchMesuresFromDB(location);
                }


            });
            }
            },REFRESH_DELAY, TimeUnit.MILLISECONDS);
        }

    public void recherche(){
        List<Measurement> res= new Select()
                .from(Measurement.class)
                .execute();

        EventBusManager.BUS.post(new MeasurementResultEvent(res));
    }

        public void searchMesuresFromDB (String search){
            List<Measurement> matchingMesureFromDB = new Select()
                    .from(Measurement.class)
                    .where("location LIKE '%" + search + "%'").orderBy("location ").execute();

            EventBusManager.BUS.post(new MeasurementResultEvent(matchingMesureFromDB));
        }

    public  void searchRechMesures(final String location, final String city) {

        if (mLastScheduleTask != null && !mLastScheduleTask.isDone()) {
            mLastScheduleTask.cancel(true);
        }

        mLastScheduleTask = mScheduler.schedule(new Runnable() {

            public void run(){
                searchRechFromDB(location);

                // Call to the REST service
                //Modification pour uniquement les locations en France
                mMesureSearchRESTService.searchForMesures("FR",city, location).enqueue(new Callback<MeasurementResult>() {
                    @Override
                    public void onResponse(Call<MeasurementResult> call, retrofit2.Response<MeasurementResult> response) {


                        // Post an event so that listening activities can update their UI
                        if (response.body() != null && response.body().results != null) {

                            ActiveAndroid.beginTransaction();
                            for (Measurement mesure : response.body().results) {

                                Measurement m = new Measurement();
                                m.location=mesure.location;
                                m.city=mesure.city;
                                List<Values> value = new ArrayList<>();

                                for(Values mv :mesure.measurements){
                                    value.add(mv);
                                }

                                m.mesure= gson.toJson(value);
                                m.save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();




                        } else {
                            // Null result
                            // We may want to display a warning to user (e.g. Toast)
                        }
                    }


                    @Override
                    public void onFailure(Call<MeasurementResult> call, Throwable t) {
                        // Request has failed or is not at expected format
                        // We may want to display a warning to user (e.g. Toast)
                        Log.e("[PlaceSearcher] [REST]", "Response error : " + t.getMessage());
                        searchRechFromDB(location);
                    }


                });
            }
        },REFRESH_DELAY, TimeUnit.MILLISECONDS);
    }


    public void searchRechFromDB (String search){
        List<Measurement> matchingMesureFromDB = new Select()
                .from(Measurement.class)
                .where(search).orderBy("location ").execute();

        EventBusManager.BUS.post(new MeasurementResultEvent(matchingMesureFromDB));
    }

        // Service describing the REST APIs
        public interface MeasurementSearchRESTService {
            @GET("latest")
            Call<MeasurementResult> searchForMesures(@Query("country") String country,@Query("city") String city, @Query("location") String location );
        }

}


