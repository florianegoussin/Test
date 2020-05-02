package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

public class ListeActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.activity_main_search_adress_edittext)
    EditText mSearchEditText;

    @BindView(R.id.activity_main_loader)
    ProgressBar mProgressBar;

    private LocationAdapter mLocationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        ButterKnife.bind(this);

        mLocationAdapter = new LocationAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mLocationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (getIntent().hasExtra("city")) {
            mSearchEditText.setText(getIntent().getStringExtra("city"));
        }

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing to do when texte is about to change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // While text is changing, hide list and show loader
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Once text has changed
                // Show a loader
                mProgressBar.setVisibility(View.VISIBLE);
                LocationSearchService.INSTANCE.searchLocationsFromAddress(editable.toString());
            }
        });

            //initDB
            Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
            Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.edmtRoomDatabase.favoriteDAO()));



    }

    @Override
    protected  void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);
        LocationSearchService.INSTANCE.searchLocationsFromAddress(mSearchEditText.getText().toString());
    }

    @Override
    protected void onPause() {
        // Unregister from Event bus : if event are posted now, the activity will not receive it
        EventBusManager.BUS.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void searchResult(final LocationResultEvent event) {
        // Here someone has posted a SearchResultEvent
        // Update adapter's model
        runOnUiThread (() -> {
            // Step 1: Update adapter's model
            mLocationAdapter.setLocations(event.getLocations());
            mLocationAdapter.notifyDataSetChanged();
            // Step 2: hide loader
            mProgressBar.setVisibility(View.GONE);
        });
    }

    @OnClick(R.id.activity_main_switch_button)
    public void clickedOnSwitchToMap(){
        Intent switchToMapIntent = new Intent (this, MapActivity.class);
        switchToMapIntent.putExtra("city", mSearchEditText.getText().toString());
        startActivity(switchToMapIntent);
    }


    @OnClick(R.id.icon_menu)
    public void clickedOnSwitchToMenu(){
        Intent switchToMenu = new Intent (this, MenuActivity.class);
        startActivity(switchToMenu);
    }

}
