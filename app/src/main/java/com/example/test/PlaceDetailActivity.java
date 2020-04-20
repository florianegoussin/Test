package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceDetailActivity extends AppCompatActivity {

    @BindView(R.id.activity_detail_place_street)
    TextView mPlaceStreet;
    private String mPlaceStreetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        mPlaceStreetValue = getIntent().getStringExtra("placeStreet");
        mPlaceStreet.setText(mPlaceStreetValue);
    }

    @OnClick(R.id.activity_detail_place_street)
    public void clickedOnPlaceStreet(){
        finish();
    }
}
