package com.example.test;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Database.DataSource.FavoriteRepository;
import com.example.test.Database.Local.EDMTRoomDatabase;
import com.example.test.Database.Local.FavoriteDataSource;
import com.example.test.event.EventBusManager;
import com.example.test.event.LocationResultEvent;
import com.example.test.event.MeasurementResultEvent;
import com.example.test.model.Location;
import com.example.test.model.Measurement;
import com.example.test.service.LocationSearchService;
import com.example.test.service.MeasurementSearchService;
import com.example.test.ui.LocationAdapter;
import com.example.test.utils.Common;
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
    List<Measurement> listMes = new ArrayList<>();
    List<Location> listLoc =new ArrayList<>();
    List<Location> locRetenu = new ArrayList<>();

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

        //initDB
        Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
        //Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.edmtRoomDatabase.CartDAO()));
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

    @OnClick(R.id.valider)
    public  void onClickRecherche(){


        parametre = new HashMap<>();


        if(!mEditBc.getText().toString().equals("")) {
            parametre.put("bc", Double.parseDouble(mEditBc.getText().toString()));
        }

        if(!mEditCo.getText().toString().equals("")) {
            parametre.put("co", Double.parseDouble(mEditCo.getText().toString()));
        }

        if(!mEditNo2.getText().toString().equals("")) {
            parametre.put("no2", Double.parseDouble(mEditNo2.getText().toString()));
        }


        if(!mEditO3.getText().toString().equals("")) {
            parametre.put("o3", Double.parseDouble(mEditO3.getText().toString()));
        }


        if(!mEditPm10.getText().toString().equals("")) {
            parametre.put("pm10", Double.parseDouble(mEditPm10.getText().toString()));
        }


        if(!mEditPm25.getText().toString().equals("")) {
            parametre.put("pm25", Double.parseDouble(mEditPm25.getText().toString()));
        }


        if(!mEditSo2.getText().toString().equals("")) {
            parametre.put("so2", Double.parseDouble(mEditSo2.getText().toString()));
        }


        System.out.println("ZONEEE: "+mZoneSearch.getText().toString());
        System.out.println("NAMEEE: "+mLocationSearch.getText().toString());
        /*if(mLocationSearch.getText().toString().equals("")){
            System.out.println("EMPTYYY");
        }
        else{
            System.out.println("FAILLL");
        }*/
        //EventBusManager.BUS.register(this);
        LocationSearchService.INSTANCE.searchRechercheFromDB(mZoneSearch.getText().toString(),mLocationSearch.getText().toString());

        //MeasurementSearchService.INSTANCE.searchRechercheFromDB(mZoneSearch.getText().toString(),mLocationSearch.getText().toString());
        //ZoneSearchService.INSTANCE.searchZoneFromDB(mZoneSearch.getText().toString());
        //LocationSearchService.INSTANCE.searchLocationsFromDB(mLocationSearch.getText().toString());

         //Intent test = new Intent(RechercheActivity.this,ListeActivity.class);
         //startActivity(test);


    }

    @Subscribe
    public void paramSearch(final MeasurementResultEvent mes){
        System.out.println("Les Mesure GET: "+mes.getmesures());
        listMes=mes.getmesures();
        System.out.println("Les Mesures autes: "+listMes);
        for (int i = 0; i < listLoc.size(); i++) {
            System.out.println("TOURR PARAM: " + i);
            System.out.println("I AM HEREEEE");
            System.out.println("LES MEsures: " + listMes);
            for (Measurement m : listMes) {
                System.out.println("I AM HEREEEE222");
                Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
                for (Measurement.Values v : valeur) {
                    System.out.println("ValueObj: " + v.value);
                    System.out.println("ValeurSaisi: " + parametre.get(v.parameter));
                    if(parametre.get(v.parameter) != null){
                        if (v.value > parametre.get(v.parameter)) {
                            locRetenu.add(listLoc.get(i));
                            System.out.println("AJOUTTTT");
                        }
                    }
                }
            }


        }
        runOnUiThread(() -> {
            System.out.println("LOCATIONSSSS: " + locRetenu);
            mLocationAdapter.setLocations(locRetenu);
            mLocationAdapter.notifyDataSetChanged();
        });

    }

    @Subscribe
    public void searchResult(final LocationResultEvent event) {
        System.out.println("IN SEARCH RESULT ");

        // Here someone has posted a SearchResultEvent
        /*HashMap<String, List<Measurement>> measurementHashmap = new HashMap<String, List<Measurement>>();
        List<Measurement> mesureList;
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
*/
        //System.out.println("HASHMAPContenu: "+ parametre.get(1));
        if(!(parametre.isEmpty())) {
             listLoc = event.getLocations();
            String nomLoc;
            String cityLoc;
            Float valParam;
            for (int i = 0; i < listLoc.size(); i++) {
                System.out.println("TOURR Result: " + i);
                nomLoc = listLoc.get(i).location;
                cityLoc = listLoc.get(i).city;
                System.out.println("NOM Location"+nomLoc);
                System.out.println("Ville Location: "+cityLoc);
                MeasurementSearchService.INSTANCE.searchMesures(nomLoc, cityLoc);
                //System.out.println("I AM HEREEEE");
                //System.out.println("LES MEsures: " + listMes);
                /*for (Measurement m : listMes) {
                    System.out.println("I AM HEREEEE222");
                    Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
                    for (Measurement.Values v : valeur) {
                        System.out.println("ValueObj: " + v.value);
                        System.out.println("ValeurSaisi: " + parametre.get(v.parameter));
                        if (v.value > parametre.get(v.parameter)) {
                            locRetenu.add(listLoc.get(i));
                        }
                    }*/
            }


            /*
            runOnUiThread(() -> {
                System.out.println("LOCATIONSSSS: " + locRetenu);
                mLocationAdapter.setLocations(locRetenu);
                mLocationAdapter.notifyDataSetChanged();
            });*/

        }
        else{
            runOnUiThread(() -> {
                System.out.println("LOCATIONSSSS22: " + event.getLocations());
                mLocationAdapter.setLocations(event.getLocations());
                mLocationAdapter.notifyDataSetChanged();
            });
        }
    }




}