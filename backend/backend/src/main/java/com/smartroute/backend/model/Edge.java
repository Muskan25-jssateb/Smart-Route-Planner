package com.smartroute.backend.model;

public class Edge {

    private final String destination;
    private final double weight;
    private final double distance;
    private final double baseTravelTime;

    public Edge(
            String destination,
            double weight,
            double distance,
            double baseTravelTime
    ) {
        this.destination = destination;
        this.weight = weight;
        this.distance = distance;
        this.baseTravelTime = baseTravelTime;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public double getDistance() {
        return distance;
    }

    public double getBaseTravelTime() {
        return baseTravelTime;
    }
}