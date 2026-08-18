package com.smartroute.backend.model;

import java.util.List;

public class RouteResult {

    private final List<String> path;
    private final double estimatedTravelTime;
    private final double totalDistance;
    private final double baseTravelTime;

    public RouteResult(
            List<String> path,
            double estimatedTravelTime,
            double totalDistance,
            double baseTravelTime
    ) {
        this.path = path;
        this.estimatedTravelTime = estimatedTravelTime;
        this.totalDistance = totalDistance;
        this.baseTravelTime = baseTravelTime;
    }

    public List<String> getPath() {
        return path;
    }

    public double getEstimatedTravelTime() {
        return estimatedTravelTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getBaseTravelTime() {
        return baseTravelTime;
    }
}