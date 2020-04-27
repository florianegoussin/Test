package com.example.test.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "LocationCoordinates")
public class LocationCoordinates extends Model implements Serializable {

    @Column(name = "location", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String location;

    @Expose
    public List<Double> coordinates = new ArrayList<>();

    @Expose
    @Column(name = "longitude")
    public double longitude = 0;

    @Expose
    @Column(name = "latitude")
    public double latitude = 0;
}
