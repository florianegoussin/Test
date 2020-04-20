package com.example.test.model;

import com.google.gson.annotations.Expose;

public class PlaceAddress {
    @Expose
    public String city;

    @Expose
    public String country;

    @Expose
    public String id;

    @Expose
    public PlaceCoordinates coordinates;

   // @Expose
 //   public PlaceParameter countsByMeasurement;
}
