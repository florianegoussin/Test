package com.example.test.event;

import com.example.test.model.PlaceAddress;

import java.util.List;

public class SearchResultEvent {

    private List<PlaceAddress> places;

    public SearchResultEvent(List<PlaceAddress> places){
        this.places = places;
    }

    public List<PlaceAddress> getPlaces(){return places;}
}
