package com.example.test.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name = "Measurement")
public class Measurement extends Model {

    @Expose
    @Column(name = "location", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String location;

    @Expose
    @Column(name = "city")
    public String city;

    @Expose
    @Column(name="country")
    public String country;

    @Expose
    public List<MeasurementValue> measurements;

}
