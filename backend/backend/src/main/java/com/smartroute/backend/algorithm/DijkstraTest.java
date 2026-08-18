package com.smartroute.backend.algorithm;

import com.smartroute.backend.model.Graph;
import com.smartroute.backend.model.RouteResult;

public class DijkstraTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        graph.addRoad("A", "B", 7, 5, 7);
        graph.addRoad("A", "C", 6, 3, 6);
        graph.addRoad("B", "D", 6, 5, 6);
        graph.addRoad("C", "D", 7, 4, 7);

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();

        RouteResult result =
                dijkstra.findShortestPath(
                        graph,
                        "A",
                        "D"
                );

        System.out.println(
                "Path: " + result.getPath()
        );

        System.out.println(
                "Travel time: " +
                        result.getEstimatedTravelTime()
        );

        System.out.println(
                "Distance: " +
                        result.getTotalDistance()
        );

    }
}