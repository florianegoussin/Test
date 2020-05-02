package com.example.test.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Database.ModelDB.Favorite;
import com.example.test.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavorisAdapter extends RecyclerView.Adapter<FavorisAdapter.FavoriteViewHolder> {

    Context context;
    List<Favorite> favorisList;

    public FavorisAdapter(Context context, List<Favorite> favorisList) {
        this.context = context;
        this.favorisList = favorisList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.favoris_item,parent,false);
        return new FavoriteViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.place_adapter_icon.setImageResource(R.drawable.home_icon);
        holder.place_adapter_country.setText(favorisList.get(position).country);
        holder.place_adapter_city.setText(favorisList.get(position).city);
        holder.place_adapter_location.setText(favorisList.get(position).location);



    }

    @Override
    public int getItemCount() {
        return favorisList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_adapter_icon)
        ImageView place_adapter_icon;

        @BindView(R.id.place_adapter_country)
        TextView place_adapter_country;

        @BindView(R.id.place_adapter_city)
        TextView place_adapter_city;

        @BindView(R.id.place_adapter_location)
        TextView place_adapter_location;


        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
