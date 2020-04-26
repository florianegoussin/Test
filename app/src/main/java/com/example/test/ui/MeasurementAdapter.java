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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
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
        Gson gson;
        gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
        for(Measurement m: mesureList) {
            Measurement.Values[] valeur = gson.fromJson(m.mesure, Measurement.Values[].class);
            for (Measurement.Values v : valeur)
            {
                holder.param.setText(v.parameter+" : "+v.value +" "+v.unit+"\n");
            }

        }
        //holder.param.setText(mesureList.get(position).mesure);
        //holder.mesure_value.setText(Double.toString(mesureList.get(position).measurements.get(position).value));

    }

    @Override
    public int getItemCount() {
        return mesureList.size();
    }

    public void setMesures(List<Measurement> mesures){
        this.mesureList = mesures;
        System.out.println("Liste"+ this.mesureList);
    }

    public class MeasurementViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mesure_parameter)
        TextView mesure_parameter;

        @BindView(R.id.mesure_value)
        TextView mesure_value;

        @BindView(R.id.param)
        TextView param;

        public MeasurementViewHolder(@NonNull View mesureView) {
            super(mesureView);
            ButterKnife.bind(this, mesureView);
        }
    }
}