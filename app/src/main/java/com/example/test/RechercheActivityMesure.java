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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechercheActivityMesure extends AppCompatActivity {

    private ViewSwitcher simpleViewSwitcher;
    Button btnNext, btnPrev;

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
    List<Measurement> MesRetenu = new ArrayList<>();
    String zone;
    String name;

    private LocationAdapter mLocationAdapter;
    private Gson gson ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_mesure);

        btnNext = (Button) findViewById(R.id.buttonNext);
        btnPrev = (Button) findViewById(R.id.buttonPrevious);
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.simpleViewSwitcher); // get the reference of ViewSwitcher


        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // show the next view of ViewSwitcher
                simpleViewSwitcher.showNext();

                //Affectation des valeures des mesures saisies
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

                zone =mZoneSearch.getText().toString();
                name=mLocationSearch.getText().toString();


                //Récupération des mesures
                LocationSearchService.INSTANCE.searchRechercheFromDB(mZoneSearch.getText().toString(),mLocationSearch.getText().toString());

            }
        });


        btnPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                locRetenu.clear();
                simpleViewSwitcher.showPrevious();
                MesRetenu.clear();
                listMes.clear();
                listLoc.clear();

            }
        });

        ButterKnife.bind(this);

        mLocationAdapter = new LocationAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mLocationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();

        //initDB
        Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.edmtRoomDatabase.favoriteDAO()));

    }




    @Override
    protected void onResume() {
        super.onResume();
        EventBusManager.BUS.register(this);
        mLocationAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBusManager.BUS.unregister(this);
    }


    @Subscribe
    public void paramSearch(final MeasurementResultEvent mes){
        //Recherche en fonction des mesures et des zones et/ou des noms
        if(!(parametre.isEmpty()) && ( (!zone.isEmpty() || !name.isEmpty()) || (!zone.isEmpty() && !name.isEmpty()))) {

            //Déclaration et affectation des variables
            Boolean test=false;
            Boolean res;
            ArrayList<Boolean> valTest= new ArrayList<Boolean>();
            ArrayList<Boolean> resultat= new ArrayList<Boolean>();
            int cpt=-1;


            listMes = mes.getmesures();

            //Parcourir les locations
            for (int i = 0; i < listLoc.size(); i++) {
                Measurement m = listMes.get(i);
                Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
                //Parcourir le tableau de mesure saisi par l'utilisateur
                for (Map.Entry mapentry : parametre.entrySet()) {
                    test=false;
                    cpt++;
                    if(mapentry.getKey() != null) {
                       //Parcourir les paramètres des objets Measurement
                        for (int j = 0; j < valeur.length; j++) {
                            if (!test) {
                                //Si les deux paramètres correspondent, on procède à la comparaison
                                if (mapentry.getKey().equals(valeur[j].parameter)) {
                                    if (valeur[j].value > parametre.get(valeur[j].parameter)) {
                                        test=true;
                                    }
                                    else {
                                        test = false;
                                    }
                                }
                                else{
                                    test=false;
                                }
                                valTest.add(test);
                            }
                            else{
                                break;
                            }

                        }

                        //Ajouter les objets Meausrement qui respecte les filtres de la recherche dans le tableau resultat
                        for(int k=0;k<valTest.size();k++) {
                            if(valTest.get(k)){
                                resultat.add(valTest.get(k));
                                valTest.clear();
                                break;
                            }
                            else{
                                if(k == valTest.size()-1){
                                    resultat.add(false);
                                    valTest.clear();
                                }
                            }
                        }

                    }

                }

                //Ajouter les locations retenu dans le tableau locRetenu grâce à la valeur du booléen res
                res=resultat.get(0);
                for(int l=1;l<resultat.size();l++){
                    res = res && resultat.get(l);
                }
                resultat.clear();
                if(res){
                    locRetenu.add(listLoc.get(i));
                }

            }
            runOnUiThread(() -> {
                mLocationAdapter.setLocations(locRetenu);
                mLocationAdapter.notifyDataSetChanged();
            });
        }
        //Recherche en fonction des mesures uniquement
        else {

            //Declaration et affectation des variables
            String nomLocMes;
            String cityLocMes;
            String chaineLocLike = "Location.location LIKE '%";
            String chaineCityLike = "(city LIKE '%";
            String Req="";
            Boolean test=false;
            Boolean res;
            ArrayList<Boolean> valTest= new ArrayList<Boolean>();
            ArrayList<Boolean> resultat= new ArrayList<Boolean>();
            int cpt=-1;

            listMes = mes.getmesures();

            //Parcourir list mesures et faire la comparaison de valeur
            for (int i = 0; i < listMes.size(); i++) {
                cpt=-1;
                test=false;
                Measurement m = listMes.get(i);
                Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
                //Parcourir le tableau de mesure saisi par l'utilisateur
                for (Map.Entry mapentry : parametre.entrySet()) {
                    test=false;
                    cpt++;
                    if(mapentry.getKey() != null) {
                        //Parcourir les paramètres des objets Measurement
                        for (int j = 0; j < valeur.length; j++) {
                            if (!test) {
                                //Si les deux paramètres correspondent, on procède à la comparaison
                                if (mapentry.getKey().equals(valeur[j].parameter)) {
                                    if (valeur[j].value > parametre.get(valeur[j].parameter)) {
                                        test=true;
                                    } else {
                                        test = false;
                                    }
                                }
                                else{
                                    test=false;
                                }
                                valTest.add(test);

                            }
                            else{
                                break;
                            }

                        }

                        //Ajouter les objets Meausrement qui respecte les filtres de la recherche dans le tableau resultat
                        for(int k=0;k<valTest.size();k++) {
                            if(valTest.get(k)){
                                resultat.add(valTest.get(k));
                                valTest.clear();
                                break;
                            }
                            else{
                                if(k == valTest.size()-1){
                                    resultat.add(false);
                                    valTest.clear();
                                }
                            }
                        }
                    }

                }

                //Ajouter les Measurement retenu dans le tableau MesRetenu grâce à la valeur du booléen res
                res=resultat.get(0);
                for(int l=1;l<resultat.size();l++){
                    res = res && resultat.get(l);
                }
                resultat.clear();
                if(res){
                    MesRetenu.add(listMes.get(i));
                }

            }

            //Parcourir les Measurements retenu pour récuppérer les locations correspondantes à ceux là.
            for (int j = 0; j < MesRetenu.size(); j++) {
                nomLocMes = MesRetenu.get(j).location;
                cityLocMes = MesRetenu.get(j).city;

                if (j == MesRetenu.size() - 1) {
                    Req= Req + chaineCityLike + cityLocMes + "%' AND " + chaineLocLike + nomLocMes + "%')";
                } else {
                    Req = Req + chaineCityLike + cityLocMes + "%' AND " + chaineLocLike + nomLocMes + "%') OR ";
                }

            }
            if(!MesRetenu.isEmpty()){
                LocationSearchService.INSTANCE.searchRechercheFromDB(Req, Req);
            }

        }

    }

    @Subscribe
    public void searchResult(final LocationResultEvent event) {
        //Recherche en fonction des mesures et des zones et/ou des noms
        if(MesRetenu.isEmpty()) {
            if (!(parametre.isEmpty()) && ((!zone.isEmpty() || !name.isEmpty()) || (!zone.isEmpty() && !name.isEmpty()))) {
                listLoc = event.getLocations();
                String nomLoc;
                String cityLoc;
                String chaineLocLike = "location LIKE '%";
                String ReqnomLoc = "";
                String ReqcityLoc = "";
                Float valParam;

                //Parcourir la liste des locations pour récuppérer leurs objets mesures
                for (int i = 0; i < listLoc.size(); i++) {

                    nomLoc = listLoc.get(i).location;
                    cityLoc = listLoc.get(i).city;

                    if (i == listLoc.size() - 1) {
                        ReqnomLoc = ReqnomLoc + chaineLocLike + nomLoc + "%'";
                    } else {
                        ReqnomLoc = ReqnomLoc + chaineLocLike + nomLoc + "%' OR ";
                    }

                }
                MeasurementSearchService.INSTANCE.searchRechMesures(ReqnomLoc, ReqcityLoc);


            }
            //Recherche en fonction des mesures uniquement
            else if (!(parametre.isEmpty()) && (zone.isEmpty() && name.isEmpty())) {
                MeasurementSearchService.INSTANCE.recherche();

            }
            else {
                runOnUiThread(() -> {
                    mLocationAdapter.setLocations(event.getLocations());
                    mLocationAdapter.notifyDataSetChanged();
                });
            }
        }
        //Recherche en fonction des zones et/ou noms
        else{
            listLoc = event.getLocations();
            runOnUiThread(() -> {
                mLocationAdapter.setLocations(event.getLocations());
                mLocationAdapter.notifyDataSetChanged();
            });


        }
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