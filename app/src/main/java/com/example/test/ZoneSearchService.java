package com.example.test;

import android.util.Log;

import com.example.test.event.EventBusManager;
import com.example.test.event.ZoneResultEvent;
import com.example.test.model.ZoneSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ZoneSearchService {


    public static ZoneSearchService INSTANCE = new ZoneSearchService();
    private final PlaceSearchRESTService mPlaceSearchRESTService;


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

    public  void searchPlacesFromAddress(final String search) {

                // Call to the REST service
                mPlaceSearchRESTService.searchForPlaces(search).enqueue(new Callback<ZoneSearchResult>() {
                    @Override
                    public void onResponse(Call<ZoneSearchResult> call, retrofit2.Response<ZoneSearchResult> response) {
                        // Post an event so that listening activities can update their UI
                        if (response.body() != null && response.body().results != null) {
                            EventBusManager.BUS.post(new ZoneResultEvent(response.body().results));


                        } else {
                            // Null result
                            // We may want to display a warning to user (e.g. Toast)
                        }
                    }


                    @Override
                    public void onFailure(Call<ZoneSearchResult> call, Throwable t) {
                        // Request has failed or is not at expected format
                        // We may want to display a warning to user (e.g. Toast)
                        Log.e("[PlaceSearcher] [REST]", "Response error : " + t.getMessage());
                    }
                });
            }

        // Service describing the REST APIs
        public interface PlaceSearchRESTService {
            @GET("cities/")
            Call<ZoneSearchResult> searchForPlaces(@Query("country") String search);

        }


}
