package com.example.test.model;

import android.view.Display;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

@Table(name = "Measurement")
public class Measurement extends Model {

    @Expose
    @Column(name = "location", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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
