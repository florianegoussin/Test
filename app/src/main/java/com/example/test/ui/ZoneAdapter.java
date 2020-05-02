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
import com.example.test.R;
import com.example.test.model.ZoneAddress;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder>{
    private LayoutInflater inflater;
    private Activity activity;
    private List<ZoneAddress> mZones;

    public ZoneAdapter(Activity activity, List<ZoneAddress> zones) {
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.mZones = zones;
    }

    @Override
    public ZoneAdapter.ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.zone_item, parent, false);
        ZoneAdapter.ZoneViewHolder holder = new ZoneAdapter.ZoneViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ZoneAdapter.ZoneViewHolder holder, int position) {
        // Adapt the ViewHolder state to the new element
        final ZoneAddress zone = mZones.get(position);
        holder.mPlaceCountryTextView.setText(zone.country);
        holder.mPlaceCityTextView.setText(zone.city);


        //Accès au détail de l'activité, doit renvoyer les mêmes infos qu'au clic sur l'infowindows
        holder.mPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ListeActivity = new Intent(activity, ListeActivity.class);
                ListeActivity.putExtra("city", zone.city);
                activity.startActivity(ListeActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mZones.size();
    }

    public void setPlaces(List<ZoneAddress> places){
        this.mZones = places;
    }

    // Pattern ViewHolder :
    class ZoneViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.place_adapter_icon)
        ImageView mPlaceIcon;

        @BindView(R.id.place_adapter_country)
        TextView mPlaceCountryTextView;

        @BindView(R.id.place_adapter_city)
        TextView mPlaceCityTextView;


        public ZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

