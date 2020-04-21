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
import com.example.test.event.ZoneResultEvent;
import com.example.test.model.ZoneAddress;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.activity_main_search_adress_edittext)
    EditText mSearchEditText;

    @BindView(R.id.activity_main_loader)
    ProgressBar mProgressBar;

    private GoogleMap mActiveGoogleMap;
    private Map<String, ZoneAddress> mMarkersToPlaces = new LinkedHashMap<>();

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
                ZoneSearchService.INSTANCE.searchPlacesFromAddress(editable.toString());
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);

        ZoneSearchService.INSTANCE.searchPlacesFromAddress(mSearchEditText.getText().toString());
    }

    @Override
    protected void onPause(){
        EventBusManager.BUS.unregister(this);

        super.onPause();
    }

    @Subscribe
    public void searchResult(final ZoneResultEvent event){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Here someone has posted a SearchResultEvent
                // Check that map is ready
                if (mActiveGoogleMap != null) {
                    // Update map's markers
                    mActiveGoogleMap.clear();
                    mMarkersToPlaces.clear();
                    for (ZoneAddress place : event.getPlaces()) {
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
                       /* MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(place.latitude, place.getCoordinates().longitude))
                                .title(place.city)
                                .snippet(place.country + " " + place.location);
                                //.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));*/

                        // Step 3: add marker
                        //mActiveGoogleMap.addMarker(markerOptions);
                        /*Marker marker = mActiveGoogleMap.addMarker(markerOptions);
                        mMarkersToPlaces.put(marker.getId(), place);*/
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
                ZoneAddress associatedPlace = mMarkersToPlaces.get(marker.getId());
                if (associatedPlace != null) {
                    Intent seePlaceDetailIntent = new Intent(MapActivity.this, PlaceDetailActivity.class);
                    seePlaceDetailIntent.putExtra("placeStreet", associatedPlace.country);
                    startActivity(seePlaceDetailIntent);
                }
            }
        });
    }
}
