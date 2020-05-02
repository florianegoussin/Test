package com.example.test.event;

import com.example.test.model.Location;

import java.util.List;

public class LocationResultEvent {
    private List<Location> locations;


    public LocationResultEvent(List<Location> locations){
        this.locations = locations;
    }

    public List<Location> getLocations(){return locations;}

}
