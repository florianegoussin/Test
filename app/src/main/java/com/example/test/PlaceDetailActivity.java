package com.example.test;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.event.EventBusManager;
import com.example.test.ui.MeasurementAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaceDetailActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView_param)
    RecyclerView recyclerView_param;

    @BindView(R.id.place_adapter_icon)
    ImageView place_adapter_icon;

    @BindView(R.id.place_adapter_location)
    TextView place_adapter_location;

    @BindView(R.id.place_adapter_country)
    TextView place_adapter_country;

    @BindView(R.id.place_adapter_city)
    TextView place_adapter_city;


    private MeasurementAdapter mMesureAdapter;


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

        mMesureAdapter = new MeasurementAdapter(this, new ArrayList<>());
        recyclerView_param.setAdapter(mMesureAdapter);
        recyclerView_param.setLayoutManager(new LinearLayoutManager(this));

        place_adapter_icon.setImageResource(R.drawable.home_icon);
        place_adapter_country.setText(getIntent().getStringExtra("country"));
        place_adapter_city.setText(getIntent().getStringExtra("city"));
        place_adapter_location.setText(getIntent().getStringExtra("location"));

        //mPlaceStreetValue = getIntent().getStringExtra("placeStreet");
        //mPlaceStreet.setText(mPlaceStreetValue);



        //place_adapter_id.setText();




    }

    @Override
    protected  void onResume(){
        super.onResume();

        EventBusManager.BUS.register(this);

    }

    @Override
    protected void onPause() {
        // Unregister from Event bus : if event are posted now, the activity will not receive it
        EventBusManager.BUS.unregister(this);
        super.onPause();
    }

    /*@OnClick(R.id.activity_detail_place_street)
    public void clickedOnPlaceStreet(){
        finish();
    }*/
}