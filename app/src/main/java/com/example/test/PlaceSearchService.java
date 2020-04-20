package com.example.test;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.test.event.EventBusManager;
import com.example.test.event.SearchResultEvent;
import com.example.test.model.Place;
import com.example.test.model.PlaceSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class PlaceSearchService {

    public static PlaceSearchService INSTANCE = new PlaceSearchService();
    private final PlaceSearchRESTService mPlaceSearchRESTService;

    private PlaceSearchService(){
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

    public  void searchPlacesFromAddress(final String search){
        // Call to the REST service
        mPlaceSearchRESTService.searchForPlaces(search).enqueue(new Callback<PlaceSearchResult>() {
            @Override
            public void onResponse(Call<PlaceSearchResult> call, retrofit2.Response<PlaceSearchResult> response) {
                // Post an event so that listening activities can update their UI
                if (response.body() != null && response.body().results != null) {
                    EventBusManager.BUS.post(new SearchResultEvent(response.body().results));
                } else {
                    // Null result
                    // We may want to display a warning to user (e.g. Toast)
                }
            }
                @Override
                public void onFailure(Call<PlaceSearchResult> call, Throwable t) {
                    // Request has failed or is not at expected format
                    // We may want to display a warning to user (e.g. Toast)
                }
            });
        }


        // Service describing the REST APIs
        public interface PlaceSearchRESTService {
            @GET("locations/")
            Call<PlaceSearchResult> searchForPlaces(@Query("city") String search);
        }


}
