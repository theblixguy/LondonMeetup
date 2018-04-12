package com.suyashsrijan.londonmeetup.models;


public class BikeMarkerData {

    private String stopName;
    private int bikesAvailable;
    private int bikesTaken;
    private int totalBikes;

    public BikeMarkerData(String stopName, int bikesAvailable, int bikesTaken, int totalBikes) {
        this.stopName = stopName;
        this.bikesAvailable = bikesAvailable;
        this.bikesTaken = bikesTaken;
        this.totalBikes = totalBikes;
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
}
