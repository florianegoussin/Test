package com.example.test.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.PlaceDetailActivity;
import com.example.test.R;
import com.example.test.model.Person;
import com.example.test.model.Place;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{
    private LayoutInflater inflater;
    private Activity context;
    private List<Place> mPlaces;

    public PlaceAdapter(Activity context, List<Place> places) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mPlaces = places;
    }

    @Override
    public PlaceAdapter.PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.place_item, parent, false);
        PlaceAdapter.PlaceViewHolder holder = new PlaceAdapter.PlaceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlaceAdapter.PlaceViewHolder holder, int position) {
        // Adapt the ViewHolder state to the new element
        final Place place = mPlaces.get(position);
        holder.mPlaceStreetTextView.setText(place.getStreet());
        holder.mPlaceZipTextView.setText(place.getZipCode());
        holder.mPlaceCityTextView.setText(place.getCity());

        holder.mPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seePlaceDetailIntent = new Intent(context, PlaceDetailActivity.class);
                seePlaceDetailIntent.putExtra("placeStreet", place.getStreet());
                context.startActivity(seePlaceDetailIntent);
            }
        });
        //holder.mPlaceIcon.setOnClickListener(new View.OnClickListener() {
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public void setPlaces(List<Place> places){
        this.mPlaces = places;
    }

    // Pattern ViewHolder :
    class PlaceViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.place_adapter_street)
        TextView mPlaceStreetTextView;

        @BindView(R.id.place_adapter_zip)
        TextView mPlaceZipTextView;

        @BindView(R.id.place_adapter_city)
        TextView mPlaceCityTextView;

        @BindView(R.id.place_adapter_icon)
        ImageView mPlaceIcon;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
