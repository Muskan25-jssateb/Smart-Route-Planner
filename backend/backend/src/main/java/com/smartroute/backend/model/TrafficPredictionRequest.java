package com.smartroute.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrafficPredictionRequest {

    private int hour;

    @JsonProperty("day_of_week")
    private int dayOfWeek;

    private String weather;

    @JsonProperty("road_type")
    private String roadType;

    @JsonProperty("base_travel_time")
    private double baseTravelTime;

    public TrafficPredictionRequest() {
    }

    public TrafficPredictionRequest(
            int hour,
            int dayOfWeek,
            String weather,
            String roadType,
            double baseTravelTime
    ) {
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
        this.weather = weather;
        this.roadType = roadType;
        this.baseTravelTime = baseTravelTime;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public double getBaseTravelTime() {
        return baseTravelTime;
    }

    public void setBaseTravelTime(double baseTravelTime) {
        this.baseTravelTime = baseTravelTime;
    }
}