package com.example.test.model;

public class Place {
    private double latitude;
    private double longitude;
    private String street;
    private String zipCode;
    private String city;

    public Place(double latitude, double longitude, String street, String zipCode, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
