package com.suyashsrijan.londonmeetup.API.tfl.models;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes;

public class Tube {

    private Stoptypes stopType;
    private String stopName;
    private String stopId;
    private LatLng coordinates;

    public Tube(Stoptypes stopType, String stopName, String stopId, LatLng coordinates) {
        this.stopType = stopType;
        this.stopName = stopName;
        this.stopId = stopId;
        this.coordinates = coordinates;
    }

    public Stoptypes getStopType() {
        return this.stopType;
    }

    public String getStopName() {
        return this.stopName;
    }

    public LatLng getCoordinates() {
        return this.coordinates;
    }

    public String getStopId() {
        return this.stopId;
    }

}
