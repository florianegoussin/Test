package com.example.test.service;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.example.test.event.EventBusManager;
import com.example.test.event.LocationResultEvent;
import com.example.test.model.Location;
import com.example.test.model.LocationCoordinates;
import com.example.test.model.LocationSearchResult;
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

public class LocationSearchService {

    private static final long REFRESH_DELAY = 650;
    public static LocationSearchService INSTANCE = new LocationSearchService();
    private final LocationSearchRESTService mLocationSearchRESTService;
    private ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture mLastScheduleTask;

    private LocationSearchService(){
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
        mLocationSearchRESTService = retrofit.create(LocationSearchRESTService.class);

    }

    public  void searchLocationsFromAddress(final String search) {

        if (mLastScheduleTask != null && !mLastScheduleTask.isDone()) {
            mLastScheduleTask.cancel(true);
        }


        // Schedule a network call in REFRESH_DELAY ms
        mLastScheduleTask = mScheduler.schedule(new Runnable() {
            public void run() {
                searchLocationsFromDB(search);

                // Call to the REST service
                //Modification pour uniquement les locations en France
                mLocationSearchRESTService.searchForLocations("FR",search).enqueue(new Callback<LocationSearchResult>() {
                    @Override
                    public void onResponse(Call<LocationSearchResult> call, retrofit2.Response<LocationSearchResult> response) {
                        // Post an event so that listening activities can update their UI
                        if (response.body() != null && response.body().results != null) {
                            //EventBusManager.BUS.post(new LocationResultEvent(response.body().results));
                            ActiveAndroid.beginTransaction();
                            for (Location location : response.body().results) {
                                Location l = new Location();
                                l.location = location.location;
                                l.city = location.city;
                                l.country = location.country;
                                l.count = location.count;

                                LocationCoordinates c = new LocationCoordinates();
                                c.longitude = location.coordinates.longitude;
                                c.latitude = location.coordinates.latitude;
                                c.save();

                                l.coordinates = c;
                                l.save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();

                            searchLocationsFromDB(search);

                        } else {
                            // Null result
                            // We may want to display a warning to user (e.g. Toast)
                        }
                    }


                    @Override
                    public void onFailure(Call<LocationSearchResult> call, Throwable t) {
                        // Request has failed or is not at expected format
                        // We may want to display a warning to user (e.g. Toast)

                        searchLocationsFromDB(search);
                    }

                });
            }

        },REFRESH_DELAY, TimeUnit.MILLISECONDS);
    }




    public void searchLocationsFromDB (String search){
        List<Location> matchingLocationFromDB = new Select()
                .from(Location.class)
                .join(LocationCoordinates.class)
                .on("Location.coordinates=LocationCoordinates.Id")
                .where("city LIKE '%" + search + "%'").orderBy("city ").execute();


        EventBusManager.BUS.post(new LocationResultEvent(matchingLocationFromDB));
    }

    public void searchRechercheFromDB(String zoneCity, String locationName) {
        /*List<Location> matchingRechFromDB=null;
        if(locationName == " " && zoneCity != " "){
            System.out.println("MARK111");
            matchingRechFromDB = new Select()
                    .from(Location.class)
                    .join(LocationCoordinates.class)
                    .on("Location.coordinates=LocationCoordinates.Id")
                    .where("city LIKE '%" + zoneCity + "%'").orderBy("city ").execute();
        }
        else if( locationName != " " && zoneCity == " "){
            System.out.println("MARK222");
            matchingRechFromDB = new Select()
                    .from(Location.class)
                    .join(LocationCoordinates.class)
                    .on("Location.coordinates=LocationCoordinates.Id")
                    .where("location LIKE '%" + locationName + "%'").execute();
        }
        else if(locationName != " " && zoneCity != " "){
            System.out.println("MARK333");
            matchingRechFromDB = new Select()
                    .from(Location.class)
                    .join(LocationCoordinates.class)
                    .on("Location.coordinates=LocationCoordinates.Id")
                    .where("city LIKE '%" + zoneCity + "%'")
                    .where("location LIKE '%" + locationName + "%'").execute();

        }*/

        List<Location> matchingRechFromDB = new Select()
                .from(Location.class)
                .join(LocationCoordinates.class)
                .on("Location.coordinates=LocationCoordinates.Id")
                .where("city LIKE '" + zoneCity + "%'")
                /*.where("location LIKE '%" + locationName + "%'")*/.execute();

        EventBusManager.BUS.post(new LocationResultEvent(matchingRechFromDB));
    }



    // Service describing the REST APIs
    public interface LocationSearchRESTService {
        @GET("locations/")
        Call<LocationSearchResult> searchForLocations(@Query("country") String country,@Query("city") String search);

    }




}


