package com.example.test.event;

import com.example.test.model.ZoneAddress;

import java.util.List;

public class ZoneResultEvent {

    private List<ZoneAddress> zones;

    public ZoneResultEvent(List<ZoneAddress> places){
        this.zones = places;
    }

    public List<ZoneAddress> getZones(){return zones;}
}
