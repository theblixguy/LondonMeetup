package com.suyashsrijan.londonmeetup.API.tfl.models;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Placetypes;

public class Bike {

    private Placetypes placeType;
    private String stopName;
    private int bikesAvailable;
    private int bikesTaken;
    private int totalBikes;
    private LatLng coordinates;

    public Bike(Placetypes placeType, String stopName, int bikesAvailable, int bikesTaken, int totalBikes, LatLng coordinates) {
        this.placeType = placeType;
        this.stopName = stopName;
        this.bikesAvailable = bikesAvailable;
        this.bikesTaken = bikesTaken;
        this.totalBikes = totalBikes;
        this.coordinates = coordinates;
    }

    public Placetypes getPlaceType() {
        return placeType;
    }

    public String getStopName() {
        return stopName;
    }

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    public int getBikesTaken() {
        return bikesTaken;
    }

    public int getTotalBikes() {
        return totalBikes;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}
