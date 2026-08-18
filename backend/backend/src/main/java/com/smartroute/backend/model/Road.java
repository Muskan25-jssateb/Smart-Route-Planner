package com.smartroute.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roads")
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private Location source;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Location destination;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private double baseTravelTime;

    @Column(nullable = false)
    private String roadType;

    public Road() {
    }

    public Road(
            Location source,
            Location destination,
            double distance,
            double baseTravelTime,
            String roadType
    ) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.baseTravelTime = baseTravelTime;
        this.roadType = roadType;
    }

    public Long getId() {
        return id;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getBaseTravelTime() {
        return baseTravelTime;
    }

    public void setBaseTravelTime(double baseTravelTime) {
        this.baseTravelTime = baseTravelTime;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }
}