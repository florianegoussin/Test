package com.example.test.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class PlaceCoordinates {
    @Expose
    public List<Double> coordinates = new ArrayList<>();

    @Expose
    public double longitude;

    @Expose
    public double latitude;
}
