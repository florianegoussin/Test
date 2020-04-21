package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.test.event.EventBusManager;
import com.example.test.event.ZoneResultEvent;
import com.example.test.ui.PlaceAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private PlaceAdapter mPlaceAdapter;

    @BindView(R.id.activity_main_search_adress_edittext)
    EditText mSearchEditText;

    @BindView(R.id.activity_main_loader)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPlaceAdapter = new PlaceAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mPlaceAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra("currentSearch")) {
            mSearchEditText.setText(getIntent().getStringExtra("currentSearch"));
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

                // Launch a search through the PlaceSearchService
                ZoneSearchService.INSTANCE.searchPlacesFromAddress(editable.toString());
            }
        });


    }

    @Override
    protected  void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);

        ZoneSearchService.INSTANCE.searchPlacesFromAddress(mSearchEditText.getText().toString());
        }
    @Override
    protected void onPause() {
        // Unregister from Event bus : if event are posted now, the activity will not receive it
        EventBusManager.BUS.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void searchResult(final ZoneResultEvent event) {
        // Here someone has posted a SearchResultEvent
        // Update adapter's model
        runOnUiThread (() -> {
            // Step 1: Update adapter's model
            mPlaceAdapter.setPlaces(event.getPlaces());
            mPlaceAdapter.notifyDataSetChanged();
            // Step 2: hide loader
            mProgressBar.setVisibility(View.GONE);
        });
    }

    @OnClick(R.id.activity_main_switch_button)
    public void clickedOnSwitchToMap(){
        Intent switchToMapIntent = new Intent (this, MapActivity.class);
        switchToMapIntent.putExtra("currentSearch", mSearchEditText.getText().toString());
        startActivity(switchToMapIntent);
    }

}
