package com.example.test;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.event.EventBusManager;
import com.example.test.event.MeasurementResultEvent;
import com.example.test.model.Measurement;
import com.example.test.service.MeasurementSearchService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Modifier;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaceDetailActivity extends AppCompatActivity {

    //@BindView(R.id.recyclerView_param)
    //RecyclerView recyclerView_param;

    @BindView(R.id.place_adapter_icon)
    ImageView place_adapter_icon;

    @BindView(R.id.place_adapter_location)
    TextView place_adapter_location;

    @BindView(R.id.place_adapter_country)
    TextView place_adapter_country;

    @BindView(R.id.place_adapter_city)
    TextView place_adapter_city;

    @BindView(R.id.param)
    TextView param;


    //private MeasurementAdapter mMesureAdapter;
    private Gson gson ;


   /* @BindView(R.id.activity_detail_place_street)
    TextView mPlaceStreet;
    private String mPlaceStreetValue;*/

    //private String mparamname;

    /*List<Measurement> listmesures;
    public PlaceDetailActivity(List<Measurement> listmesures) {
        this.listmesures = listmesures;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        this.gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
        //mMesureAdapter = new MeasurementAdapter(this, new ArrayList<>());
        //recyclerView_param.setAdapter(mMesureAdapter);
        //recyclerView_param.setLayoutManager(new LinearLayoutManager(this));




        EventBusManager.BUS.register(this);
        place_adapter_icon.setImageResource(R.drawable.home_icon);
        place_adapter_country.setText(getIntent().getStringExtra("country"));
        place_adapter_city.setText(getIntent().getStringExtra("city"));
        place_adapter_location.setText(getIntent().getStringExtra("location"));



   //     MeasurementSearchService.INSTANCE.searchMesures(getIntent().getStringExtra("city"), getIntent().getStringExtra("location"));
        MeasurementSearchService.INSTANCE.searchMesures(getIntent().getStringExtra("location") );




        //mPlaceStreetValue = getIntent().getStringExtra("placeStreet");
        //mPlaceStreet.setText(mPlaceStreetValue);



        //place_adapter_id.setText();




    }

    @Override
    protected void onPause() {
        // Unregister from Event bus : if event are posted now, the activity will not receive it
        EventBusManager.BUS.unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Subscribe
    public void searchResult(final MeasurementResultEvent event) {
        runOnUiThread( () -> {

            for(Measurement m: event.getmesures()) {
                Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
                for (Measurement.Values v : valeur)
                {
                    param.append(v.parameter+" : "+v.value +" "+v.unit+"\n");
                }
            }
        });
       /*runOnUiThread(()-> {
           //System.out.println("eventttttttttttttttttttt "+event.results);
           List<Measurement> mesures= event.getmesures();
           mMesureAdapter.setMesures(event.getmesures());
           mMesureAdapter.notifyDataSetChanged();
           System.out.println(event.getmesures());*/



           /*for(int i=0;i<=event.getmesures().size();i++){
               param.setText(mesures.get(i));
           }

        });*/
    }

    /*@OnClick(R.id.activity_detail_place_street)
    public void clickedOnPlaceStreet(){
        finish();
    }*/
}