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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    //private LayoutInflater inflater;
    Context context;
    List<Favorite> favoriteList;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        //inflater = LayoutInflater.from(context);
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fav_item_layout,parent,false);
        return new FavoriteViewHolder(itemView);

        //View view = inflater.inflate(R.layout.fav_item_layout, parent, false);
        //FavoriteViewHolder holder = new FavoriteViewHolder(view);
        //return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
       holder.place_adapter_icon.setImageResource(R.drawable.home_icon);
        holder.place_adapter_country.setText(favoriteList.get(position).country);
        holder.place_adapter_city.setText(favoriteList.get(position).city);
        //holder.place_adapter_id.setText(favoriteList.get(position).location);
        holder.place_adapter_location.setText(favoriteList.get(position).location);



    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
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
