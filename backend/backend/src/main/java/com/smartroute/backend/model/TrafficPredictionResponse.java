package com.smartroute.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrafficPredictionResponse {

    @JsonProperty("predicted_travel_time")
    private double predictedTravelTime;

    public TrafficPredictionResponse() {
    }

    public double getPredictedTravelTime() {
        return predictedTravelTime;
    }

    public void setPredictedTravelTime(double predictedTravelTime) {
        this.predictedTravelTime = predictedTravelTime;
    }
}