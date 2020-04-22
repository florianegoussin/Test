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
import com.example.test.model.ZoneAddress;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{
    private LayoutInflater inflater;
    private Activity activity;
    private List<ZoneAddress> mPlaces;

    public PlaceAdapter(Activity activity, List<ZoneAddress> places) {
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
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
        final ZoneAddress place = mPlaces.get(position);
        holder.mPlaceIdTextView.setText(place.name);
        holder.mPlaceCountryTextView.setText(place.country);
        holder.mPlaceCityTextView.setText(place.city);
        holder.mPlaceLocationTextView.setText(Integer.toString(place.count));


        //Accès au détail de l'activité, doit renvoyer les mêmes infos qu'au clic sur l'infowindows
        holder.mPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ListeActivity = new Intent(activity, ListeActivity.class);
                ListeActivity.putExtra("placeStreet", place.city);
                activity.startActivity(ListeActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public void setPlaces(List<ZoneAddress> places){
        this.mPlaces = places;
    }

    // Pattern ViewHolder :
    class PlaceViewHolder extends RecyclerView.ViewHolder
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

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

