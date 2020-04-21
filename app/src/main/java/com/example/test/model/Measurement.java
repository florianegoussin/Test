package com.example.test.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

@Table(name = "Measurement")
public class Measurement {

    @Expose
    @Column(name = "location")
    public String location;

    @Expose
    @Column(name = "parameter")
    public String parameter;

    @Expose
    @Column(name = "value")
    public double value;

    @Expose
    @Column(name = "unit")
    public String unit;

    @Expose
    @Column(name = "city")
    public ZoneAddress city;
}
