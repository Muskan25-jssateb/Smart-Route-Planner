package com.smartroute.backend.service;

import com.smartroute.backend.model.Graph;
import com.smartroute.backend.model.Road;
import com.smartroute.backend.model.TrafficPredictionRequest;
import com.smartroute.backend.repository.RoadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    private final RoadRepository roadRepository;
    private final TrafficPredictionService trafficPredictionService;

    public GraphService(
            RoadRepository roadRepository,
            TrafficPredictionService trafficPredictionService
    ) {
        this.roadRepository = roadRepository;
        this.trafficPredictionService =
                trafficPredictionService;
    }


    public Graph buildGraph(
            int hour,
            int dayOfWeek,
            String weather
    ) {

        Graph graph =
                new Graph();


        List<Road> roads =
                roadRepository.findAll();


        for (Road road : roads) {

            String source =
                    road.getSource().getName();

            String destination =
                    road.getDestination().getName();


            // -------------------------------------
            // Create ML request
            // -------------------------------------

            TrafficPredictionRequest request =
                    new TrafficPredictionRequest(
                            hour,
                            dayOfWeek,
                            weather,
                            road.getRoadType(),
                            road.getBaseTravelTime()
                    );


            // -------------------------------------
            // Ask ML model for predicted time
            // -------------------------------------

            double predictedTravelTime =
                    trafficPredictionService
                            .predictTravelTime(request);


            // -------------------------------------
            // Add traffic-aware edge
            // -------------------------------------

            graph.addRoad(
                    source,
                    destination,
                    predictedTravelTime,
                    road.getDistance(),
                    road.getBaseTravelTime()
            );


            // -------------------------------------
            // Debug information
            // -------------------------------------

            System.out.println(
                    source
                            + " -> "
                            + destination
                            + " | Distance: "
                            + road.getDistance()
                            + " km"
                            + " | Base: "
                            + road.getBaseTravelTime()
                            + " min"
                            + " | Predicted: "
                            + predictedTravelTime
                            + " min"
            );
        }


        return graph;
    }
}