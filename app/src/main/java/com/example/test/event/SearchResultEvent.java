package com.example.test.event;

import com.example.test.model.Place;

import java.util.List;

public class SearchResultEvent {

    private List<Place> places;

    public SearchResultEvent(List<Place> places){
        this.places = places;
    }

    public List<Place> getPlaces(){return places;}
}
