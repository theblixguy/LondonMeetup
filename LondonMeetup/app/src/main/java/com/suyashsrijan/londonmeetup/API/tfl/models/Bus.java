package com.suyashsrijan.londonmeetup.API.tfl.models;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes;

public class Bus {

    private Stoptypes stopType;
    private String stopName;
    private String stopIndicator;
    private String stopId;
    private LatLng coordinates;

    public Bus(Stoptypes stopType, String stopName, String stopIndicator, String stopId, LatLng coordinates) {
        this.stopType = stopType;
        this.stopName = stopName;
        this.stopIndicator = stopIndicator;
        this.stopId = stopId;
        this.coordinates = coordinates;
    }

    public Stoptypes getStopType() {
        return stopType;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopIndicator() {
        return stopIndicator;
    }

    public String getStopId() {
        return stopId;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}
