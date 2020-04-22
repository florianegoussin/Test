package com.example.test.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

@Table(name = "MeasurementValue")
class MeasurementValue {

    @Expose
    @Column(name = "parameter")
    public String parameter;

    @Expose
    @Column(name = "value")
    public int value;

    @Expose
    @Column(name = "unit")
    public String unit;

    @Expose
    @Column(name = "lastUpdated")
    public String lastUpdated;

    public MeasurementValue(){
        super();
    }

}
