package com.example.test.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.model.Measurement;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder> {

    //private LayoutInflater inflater;
    Context context;
    List<Measurement> mesureList;
    //private List<Location> mLocation;

    public MeasurementAdapter(Context context, List<Measurement> mesureList/*,List<Location> location*/) {
        this.context = context;
        this.mesureList = mesureList;
        //this.mLocation = location;
    }


    @NonNull
    @Override
    public MeasurementAdapter.MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mesureView = LayoutInflater.from(context).inflate(R.layout.parameter,parent,false);
        return new MeasurementViewHolder(mesureView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementAdapter.MeasurementViewHolder holder, int position) {
        holder.mesure_parameter.setText(mesureList.get(position).parameter);
        holder.mesure_value.setText(Double.toString(mesureList.get(position).value));

    }

    @Override
    public int getItemCount() {
        return mesureList.size();
    }

    public class MeasurementViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mesure_parameter)
        TextView mesure_parameter;

        @BindView(R.id.mesure_value)
        TextView mesure_value;

        public MeasurementViewHolder(@NonNull View mesureView) {
            super(mesureView);
            ButterKnife.bind(this, mesureView);
        }
    }
}