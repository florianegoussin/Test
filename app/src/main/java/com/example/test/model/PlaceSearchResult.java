package com.example.test.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PlaceSearchResult {
    @Expose
    public List<PlaceAddress> results;
}