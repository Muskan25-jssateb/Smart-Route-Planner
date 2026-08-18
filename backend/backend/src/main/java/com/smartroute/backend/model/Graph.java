package com.smartroute.backend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private final Map<String, List<Edge>> adjacencyList =
            new HashMap<>();

    public void addLocation(String location) {

        adjacencyList.putIfAbsent(
                location,
                new ArrayList<>()
        );
    }

    public void addRoad(
            String source,
            String destination,
            double weight,
            double distance,
            double baseTravelTime
    ) {

        addLocation(source);
        addLocation(destination);

        adjacencyList.get(source).add(
                new Edge(
                        destination,
                        weight,
                        distance,
                        baseTravelTime
                )
        );

        adjacencyList.get(destination).add(
                new Edge(
                        source,
                        weight,
                        distance,
                        baseTravelTime
                )
        );
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Edge> getNeighbors(String location) {

        return adjacencyList.getOrDefault(
                location,
                Collections.emptyList()
        );
    }

    public boolean containsLocation(String location) {

        return adjacencyList.containsKey(location);
    }
}