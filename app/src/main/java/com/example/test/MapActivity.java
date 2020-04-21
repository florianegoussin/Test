package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.test.event.EventBusManager;
import com.example.test.event.LocationResultEvent;
import com.example.test.event.ZoneResultEvent;
import com.example.test.model.Location;
import com.example.test.model.ZoneAddress;
import com.example.test.service.LocationSearchService;
import com.example.test.service.ZoneSearchService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//Ici affichage pour les locations, pas pour les zones
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.activity_main_search_adress_edittext)
    EditText mSearchEditText;

    @BindView(R.id.activity_main_loader)
    ProgressBar mProgressBar;

    private GoogleMap mActiveGoogleMap;
    private Map<String, Location> mMarkersToPlaces = new LinkedHashMap<>();


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
                //ZoneSearchService.INSTANCE.searchZone(editable.toString());
                LocationSearchService.INSTANCE.searchLocationsFromAddress(editable.toString());
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);

        LocationSearchService.INSTANCE.searchLocationsFromAddress(mSearchEditText.getText().toString());
       // ZoneSearchService.INSTANCE.searchZone(mSearchEditText.getText().toString());

    }

    @Override
    protected void onPause(){
        EventBusManager.BUS.unregister(this);

        super.onPause();
    }

    @Subscribe
    public void searchResult(final LocationResultEvent event){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Here someone has posted a SearchResultEvent
                // Check that map is ready
                if (mActiveGoogleMap != null) {
                    // Update map's markers
                    mActiveGoogleMap.clear();
                    mMarkersToPlaces.clear();

                    for (Location location : event.getLocations()) {

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(location.coordinates.latitude, location.coordinates.longitude))
                                .title(location.city)
                                .snippet(location.country + " " + location.location);
                                //.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

                        // Step 3: add marker
                        //mActiveGoogleMap.addMarker(markerOptions);
                        Marker marker = mActiveGoogleMap.addMarker(markerOptions);
                        mMarkersToPlaces.put(marker.getId(), location);
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


        //Quand on clique sur l'infoWindow pour avoir le détail de la zone
        mActiveGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Location associatedPlace = mMarkersToPlaces.get(marker.getId());
                if (associatedPlace != null) {
                    Intent seePlaceDetailIntent = new Intent(MapActivity.this, PlaceDetailActivity.class);
                    seePlaceDetailIntent.putExtra("placeStreet", associatedPlace.country);
                    startActivity(seePlaceDetailIntent);
                }
            }
        });
    }
}
