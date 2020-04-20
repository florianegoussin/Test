package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.test.event.EventBusManager;
import com.example.test.event.SearchResultEvent;
import com.example.test.model.PlaceAddress;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.activity_main_search_adress_edittext)
    EditText mSearchEditText;

    @BindView(R.id.activity_main_loader)
    ProgressBar mProgressBar;

    private GoogleMap mActiveGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        // Get map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                PlaceSearchService.INSTANCE.searchPlacesFromAddress(editable.toString());
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);

        PlaceSearchService.INSTANCE.searchPlacesFromAddress(mSearchEditText.getText().toString());
    }

    @Override
    protected void onPause(){
        EventBusManager.BUS.unregister(this);

        super.onPause();
    }

    @Subscribe
    public void searchResult(final SearchResultEvent event){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Here someone has posted a SearchResultEvent
                // Check that map is ready
                if (mActiveGoogleMap != null) {
                    // Update map's markers
                    mActiveGoogleMap.clear();
                    for (PlaceAddress place : event.getPlaces()) {
                        // Step 1: create marker icon (and resize drawable so that marker is not too big)
                       /* int markerIconResource;
                        if (place.properties.isStreet()) {
                            markerIconResource = R.drawable.street_icon;
                        } else {
                            markerIconResource = R.drawable.home_icon;
                        }*/
                       // Bitmap imageBitmap = BitmapFactory.decodeResource(getResources());
                       // Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 50, 50, false);
                        // Step 2: define marker options
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(place.coordinates.latitude, place.coordinates.longitude))
                                .title(place.city)
                                .snippet(place.country);
                                //.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

                        // Step 3: add marker
                        mActiveGoogleMap.addMarker(markerOptions);
                    }
                }
            }
        });
    }

    @OnClick(R.id.activity_map_switch_button)
    public void clickedOnSwitchToList(){
        Intent switchToListIntent = new Intent(this, MainActivity.class);
        switchToListIntent.putExtra("currentSearch", mSearchEditText.getText().toString());
        startActivity(switchToListIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mActiveGoogleMap = googleMap;
        mActiveGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mActiveGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
}
