package com.example.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Database.DataSource.FavoriteRepository;
import com.example.test.Database.Local.EDMTRoomDatabase;
import com.example.test.Database.Local.FavoriteDataSource;
import com.example.test.event.EventBusManager;
import com.example.test.event.SearchResultEvent;
import com.example.test.ui.PlaceAdapter;
import com.example.test.utils.Common;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private void initDB(){
        Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
        //Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.edmtRoomDatabase.CartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.edmtRoomDatabase.favoriteDAO()));
    }

}
