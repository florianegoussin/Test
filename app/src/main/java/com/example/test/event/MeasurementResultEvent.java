package com.example.test.event;

import com.example.test.model.Location;
import com.example.test.model.Measurement;

import java.util.List;

public class MeasurementResultEvent {

    private List<Measurement> measurement;

    public MeasurementResultEvent(List<Measurement> measurements){
        this.measurement = measurements;
    }

    public List<Measurement> getMeasurement(){return measurement;}
}
