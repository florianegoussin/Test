package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.test.event.EventBusManager;
import com.example.test.event.MeasurementResultEvent;
import com.example.test.model.Location;
import com.example.test.model.Measurement;
import com.example.test.service.LocationSearchService;
import com.example.test.service.MeasurementSearchService;
import com.example.test.service.ZoneSearchService;
import com.example.test.ui.LocationAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechercheActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.zone_search) EditText mZoneSearch;
    @BindView(R.id.location_search) EditText mLocationSearch;

    @BindView(R.id.editbc)
    EditText mEditBc;

    @BindView(R.id.editco)
    EditText mEditCo;

    @BindView(R.id.editno2)
    EditText mEditNo2;

    @BindView(R.id.edito3)
    EditText mEditO3;

    @BindView(R.id.editso2)
    EditText mEditSo2;

    @BindView(R.id.editpm10)
    EditText mEditPm10;

    @BindView(R.id.editpm25)
    EditText mEditPm25;

    HashMap<String, Double> parametre;

    private LocationAdapter mLocationAdapter;
    private Gson gson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        ButterKnife.bind(this);

        mLocationAdapter = new LocationAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mLocationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
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

    @OnClick(R.id.valider)
    public  void onClickRecherche(){


        parametre = new HashMap<>();


        if(!mEditBc.getText().toString().equals("")) {
            parametre.put("bc", Double.parseDouble(mEditBc.getText().toString()));
        }
        else parametre.put("bc", 0d);

        if(!mEditCo.getText().toString().equals("")) {
            parametre.put("co", Double.parseDouble(mEditCo.getText().toString()));
        }
        else parametre.put("co", 0d);

        if(!mEditNo2.getText().toString().equals("")) {
            parametre.put("no2", Double.parseDouble(mEditNo2.getText().toString()));
        }
        else parametre.put("no2", 0d);

        if(!mEditO3.getText().toString().equals("")) {
            parametre.put("o3", Double.parseDouble(mEditO3.getText().toString()));
        }
        else parametre.put("o3", 0d);

        if(!mEditPm10.getText().toString().equals("")) {
            parametre.put("pm10", Double.parseDouble(mEditPm10.getText().toString()));
        }
        else parametre.put("pm10", 0d);

        if(!mEditPm25.getText().toString().equals("")) {
            parametre.put("pm25", Double.parseDouble(mEditPm25.getText().toString()));
        }
        else parametre.put("pm25", 0d);

        if(!mEditSo2.getText().toString().equals("")) {
            parametre.put("so2", Double.parseDouble(mEditSo2.getText().toString()));
        }
        else parametre.put("so2", 0d);


        LocationSearchService.INSTANCE.searchRechercheFromDB(mZoneSearch.getText().toString(),mLocationSearch.getText().toString());
        //MeasurementSearchService.INSTANCE.searchRechercheFromDB(mZoneSearch.getText().toString(),mLocationSearch.getText().toString());
        //ZoneSearchService.INSTANCE.searchZoneFromDB(mZoneSearch.getText().toString());
        //LocationSearchService.INSTANCE.searchLocationsFromDB(mLocationSearch.getText().toString());

        // Intent test = new Intent(RechercheActivity.this,ListeActivity.class);
        // startActivity(test);
    }


    @Subscribe
    public void searchResult(final MeasurementResultEvent event) {


        // Here someone has posted a SearchResultEvent
        HashMap<String, List<Measurement>> measurementHashmap = new HashMap<String, List<Measurement>>();
        List<Measurement> mesureList = new ArrayList<>();
        List<Location> locationList = new ArrayList<>();
        for(Measurement mes : event.getmesures()) {

            boolean ok = true;
            for(Measurement m: event.getmesures()) { // boucle pour parcourir les mesures
                Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
                for(Measurement.Values v: valeur)
                {
                    if(parametre.get(v.parameter) > v.value) { ok = false;}

                    if(!measurementHashmap.containsKey(m.location)) { // la clée n'existe pas
                        mesureList  = new ArrayList<>();
                        measurementHashmap.put(m.location, mesureList);
                    }

                    if(m.location != null && m.mesure != null) {
                        measurementHashmap.get(m.location).add(m);
                    }
                }

                if(ok) {
                    locationList.add(mes.loc);
                }
                else {
                    measurementHashmap.remove(mes.location);
                }
            }
        }

        runOnUiThread (() -> {



            mLocationAdapter.setLocations(locationList);
            mLocationAdapter.notifyDataSetChanged();
        });
    }


}