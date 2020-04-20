package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.test.event.EventBusManager;
import com.example.test.event.SearchResultEvent;
import com.example.test.model.Person;
import com.example.test.model.Place;
import com.example.test.ui.PersonAdapter;
import com.example.test.ui.PlaceAdapter;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private PlaceAdapter mPlaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPlaceAdapter = new PlaceAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mPlaceAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected  void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);

        PlaceSearchService.INSTANCE.searchPlacesFromAddress("Loire-Atlantique");
        }
    @Override
    protected void onPause() {
        // Unregister from Event bus : if event are posted now, the activity will not receive it
        EventBusManager.BUS.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void searchResult(final SearchResultEvent event) {
        // Here someone has posted a SearchResultEvent
        // Update adapter's model
        mPlaceAdapter.setPlaces(event.getPlaces());
        runOnUiThread(() -> mPlaceAdapter.notifyDataSetChanged());
    }

}
