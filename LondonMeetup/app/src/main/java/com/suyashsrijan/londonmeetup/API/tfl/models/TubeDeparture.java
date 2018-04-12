package com.suyashsrijan.londonmeetup.API.tfl.models;

public class TubeDeparture {

    private String stationName;
    private String destinationName;
    private String lineName;
    private String goingTowards;
    private String arrivalTime;

    public TubeDeparture(String stationName, String destinationName, String lineName, String goingTowards, String arrivalTime) {
        this.stationName = stationName;
        this.destinationName = destinationName;
        this.lineName = lineName;
        this.goingTowards = goingTowards;
        this.arrivalTime = arrivalTime;
    }

    public String getStationName() {
        return stationName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getGoingTowards() {
        return goingTowards;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getLineName() {
        return lineName;
    }
}
