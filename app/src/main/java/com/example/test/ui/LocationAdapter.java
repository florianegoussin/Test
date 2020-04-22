package com.example.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.ListeActivity;
import com.example.test.PlaceDetailActivity;
import com.example.test.R;
import com.example.test.model.Location;
import com.example.test.model.ZoneAddress;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{

    private LayoutInflater inflater;
    private Activity context;
    private List<Location> mLocation;

    public LocationAdapter(Activity context, List<Location> location) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mLocation = location;
    }

    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.place_item, parent, false);
        LocationAdapter.LocationViewHolder holder = new LocationAdapter.LocationViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LocationAdapter.LocationViewHolder holder, int position) {
        // Adapt the ViewHolder state to the new element
        final Location location = mLocation.get(position);
        holder.mPlaceIdTextView.setText(location.country);
       // holder.mPlaceCountryTextView.setText(location.country);
        holder.mPlaceCityTextView.setText(location.city);
        holder.mPlaceLocationTextView.setText(Integer.toString(location.count));


        //Accès au détail de l'activité, doit renvoyer les mêmes infos qu'au clic sur l'infowindows
      /*  holder.mPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seePlaceDetailIntent = new Intent(context, PlaceDetailActivity.class);
                seePlaceDetailIntent.putExtra("placeStreet", location.city);
                context.startActivity(seePlaceDetailIntent);
            }
        })*/;

    }

    @Override
    public int getItemCount() {
        return mLocation.size();
    }

    public void setLocations(List<Location> locations){
        this.mLocation = locations;
    }

    // Pattern ViewHolder :
    class LocationViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.place_adapter_id)
        TextView mPlaceIdTextView;

        @BindView(R.id.place_adapter_country)
        TextView mPlaceCountryTextView;

        @BindView(R.id.place_adapter_city)
        TextView mPlaceCityTextView;

        @BindView(R.id.place_adapter_icon)
        ImageView mPlaceIcon;

        @BindView(R.id.place_adapter_location)
        TextView mPlaceLocationTextView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}