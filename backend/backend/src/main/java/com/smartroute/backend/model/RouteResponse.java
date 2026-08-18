package com.smartroute.backend.model;

import java.util.List;

public class RouteResponse {

    private String source;

    private String destination;

    private List<String> path;

    private double totalDistance;

    private double baseTravelTime;

    private double estimatedTravelTime;

    private double trafficIncreasePercentage;

    private String trafficLevel;

    private String weather;

    private List<AlternativeRoute> alternatives;


    public RouteResponse() {
    }


    public RouteResponse(
            String source,
            String destination,
            List<String> path,
            double totalDistance,
            double baseTravelTime,
            double estimatedTravelTime,
            double trafficIncreasePercentage,
            String trafficLevel,
            String weather,
            List<AlternativeRoute> alternatives
    ) {

        this.source = source;

        this.destination = destination;

        this.path = path;

        this.totalDistance = totalDistance;

        this.baseTravelTime = baseTravelTime;

        this.estimatedTravelTime =
                estimatedTravelTime;

        this.trafficIncreasePercentage =
                trafficIncreasePercentage;

        this.trafficLevel =
                trafficLevel;

        this.weather = weather;

        this.alternatives =
                alternatives;
    }


    public String getSource() {
        return source;
    }


    public String getDestination() {
        return destination;
    }


    public List<String> getPath() {
        return path;
    }


    public double getTotalDistance() {
        return totalDistance;
    }


    public double getBaseTravelTime() {
        return baseTravelTime;
    }


    public double getEstimatedTravelTime() {
        return estimatedTravelTime;
    }


    public double getTrafficIncreasePercentage() {
        return trafficIncreasePercentage;
    }


    public String getTrafficLevel() {
        return trafficLevel;
    }


    public String getWeather() {
        return weather;
    }


    public List<AlternativeRoute> getAlternatives() {
        return alternatives;
    }
}