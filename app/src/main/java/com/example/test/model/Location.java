package com.example.test.model;

import com.google.gson.annotations.Expose;

public class Location {
    @Expose
    public String location;

    @Expose
    public String city;

    @Expose
    public String country;

    @Expose
    public LocationCoordinates coordinates;
}
