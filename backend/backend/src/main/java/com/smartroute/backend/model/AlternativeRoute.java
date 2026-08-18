package com.smartroute.backend.model;

import java.util.List;

public class AlternativeRoute {

    private List<String> path;

    private double totalDistance;

    private double estimatedTravelTime;

    private double baseTravelTime;


    public AlternativeRoute() {
    }


    public AlternativeRoute(
            List<String> path,
            double totalDistance,
            double estimatedTravelTime,
            double baseTravelTime
    ) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.estimatedTravelTime = estimatedTravelTime;
        this.baseTravelTime = baseTravelTime;
    }


    public List<String> getPath() {
        return path;
    }


    public double getTotalDistance() {
        return totalDistance;
    }


    public double getEstimatedTravelTime() {
        return estimatedTravelTime;
    }


    public double getBaseTravelTime() {
        return baseTravelTime;
    }
}