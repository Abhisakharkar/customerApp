package com.example.chinmay.anew.model;

import com.google.android.gms.maps.model.LatLng;

public class MyGooglePlaces {
    private String name;
    private String category;
    private String rating;
    private String opennow;
    private LatLng lng;
    private String vicinity;
    private String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double latitude,longitude;
    public MyGooglePlaces()
    {
        this.name="";
        this.category="";
        this.rating="";
        this.opennow="";
        this.vicinity="";
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getOpen() {
        return opennow;
    }
    public void setOpenNow(String open) {
        this.opennow = open;
    }
    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
