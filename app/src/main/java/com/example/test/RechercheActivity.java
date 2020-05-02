package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Database.DataSource.FavoriteRepository;
import com.example.test.Database.Local.EDMTRoomDatabase;
import com.example.test.Database.Local.FavoriteDataSource;
import com.example.test.event.EventBusManager;
import com.example.test.event.LocationResultEvent;
import com.example.test.service.LocationSearchService;
import com.example.test.ui.LocationAdapter;
import com.example.test.utils.Common;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechercheActivity extends AppCompatActivity {

    private ViewSwitcher simpleViewSwitcher;
    Button btnNext, btnPrev;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.zone_search) EditText mZoneSearch;
    @BindView(R.id.location_search) EditText mLocationSearch;



    private LocationAdapter mLocationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        btnNext = (Button) findViewById(R.id.buttonNext);
        btnPrev = (Button) findViewById(R.id.buttonPrevious);
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.simpleViewSwitcher); // get the reference of ViewSwitcher

        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // show the next view of ViewSwitcher
                simpleViewSwitcher.showNext();


                System.out.println("ZONEEE: "+mZoneSearch.getText().toString());
                System.out.println("NAMEEE: "+mLocationSearch.getText().toString());

                LocationSearchService.INSTANCE.searchRechercheFromDB(mZoneSearch.getText().toString(),mLocationSearch.getText().toString());


            }
        });


        btnPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                simpleViewSwitcher.showPrevious();

            }
        });

        ButterKnife.bind(this);

        mLocationAdapter = new LocationAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mLocationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        //initRoomDB
        Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.edmtRoomDatabase.favoriteDAO()));


    }




    @Override
    protected void onResume() {
        super.onResume();
        EventBusManager.BUS.register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBusManager.BUS.unregister(this);
    }



    @Subscribe
    public void searchResult(final LocationResultEvent event) {
        System.out.println("IN SEARCH RESULT ");
            runOnUiThread(() -> {
                System.out.println("LOCATIONSSSS22: " + event.getLocations());
                mLocationAdapter.setLocations(event.getLocations());
                mLocationAdapter.notifyDataSetChanged();
            });

    }


    @OnClick(R.id.icon_menu)
    public void clickedOnSwitchToMenu(){
        Intent switchToMenu = new Intent (this, MenuActivity.class);
        startActivity(switchToMenu);
    }

    @OnClick(R.id.icon_menu2)
    public void clickedOnMenu(){
        Intent switchToMenu = new Intent (this, MenuActivity.class);
        startActivity(switchToMenu);
    }




}