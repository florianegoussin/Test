package com.example.test.service;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.example.test.event.EventBusManager;
import com.example.test.event.ZoneResultEvent;
import com.example.test.model.Location;
import com.example.test.model.ZoneAddress;
import com.example.test.model.ZoneSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
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

public class ZoneSearchService {

    private static final long REFRESH_DELAY = 650;
    public static ZoneSearchService INSTANCE = new ZoneSearchService();
    private final PlaceSearchRESTService mPlaceSearchRESTService;
    private ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture mLastScheduleTask;

    private ZoneSearchService(){
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
        mPlaceSearchRESTService = retrofit.create(PlaceSearchRESTService.class);

    }

    public  void searchZone(final String search) {

        if (mLastScheduleTask != null && !mLastScheduleTask.isDone()) {
            mLastScheduleTask.cancel(true);
        }

        mLastScheduleTask = mScheduler.schedule(new Runnable() {
            public void run() {
                searchZoneFromDB(search);

                // Call to the REST service
                mPlaceSearchRESTService.searchForZones("FR").enqueue(new Callback<ZoneSearchResult>() {
                    @Override
                    public void onResponse(Call<ZoneSearchResult> call, retrofit2.Response<ZoneSearchResult> response) {
                        // Post an event so that listening activities can update their UI
                        if (response.body() != null && response.body().results != null) {
                            ActiveAndroid.beginTransaction();
                            for (ZoneAddress zone : response.body().results) {
                                zone.save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();

                            searchZoneFromDB(search);

                        } else {
                            // Null result
                            // We may want to display a warning to user (e.g. Toast)
                        }
                    }


                    @Override
                    public void onFailure(Call<ZoneSearchResult> call, Throwable t) {
                        // Request has failed or is not at expected format
                        // We may want to display a warning to user (e.g. Toast)
                        searchZoneFromDB(search);
                    }
                });

            }
        },REFRESH_DELAY, TimeUnit.MILLISECONDS);
    }

    public void searchZoneFromDB(String search){
        List<ZoneAddress> matchingZoneFromDB = new Select().from(ZoneAddress.class)
                .where("city LIKE '%" + search + "%'").orderBy("country").execute();

        EventBusManager.BUS.post((new ZoneResultEvent(matchingZoneFromDB)));
    }

        // Service describing the REST APIs
        public interface PlaceSearchRESTService {
            @GET("cities/")
            Call<ZoneSearchResult> searchForZones(@Query("country") String search);

        }


}
