package com.example.test.event;

import com.example.test.model.Measurement;

import java.util.List;

public class MeasurementResultEvent {

    private List<Measurement> mesures;

    public MeasurementResultEvent(List<Measurement> mesures){
        this.mesures = mesures;
    }

    public List<Measurement> getmesures(){return mesures;}
}
