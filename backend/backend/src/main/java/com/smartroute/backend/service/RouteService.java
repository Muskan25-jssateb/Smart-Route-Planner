package com.smartroute.backend.service;

import com.smartroute.backend.algorithm.DijkstraAlgorithm;
import com.smartroute.backend.model.AlternativeRoute;
import com.smartroute.backend.model.Edge;
import com.smartroute.backend.model.Graph;
import com.smartroute.backend.model.RouteRequest;
import com.smartroute.backend.model.RouteResponse;
import com.smartroute.backend.model.RouteResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RouteService {

    private final GraphService graphService;

    private final DijkstraAlgorithm dijkstraAlgorithm;


    public RouteService(
            GraphService graphService
    ) {

        this.graphService =
                graphService;

        this.dijkstraAlgorithm =
                new DijkstraAlgorithm();
    }


    // =============================================
    // Find optimal route
    // =============================================

    public RouteResponse findRoute(
            RouteRequest request
    ) {

        // -----------------------------------------
        // Build traffic-aware graph
        // -----------------------------------------

        Graph graph =
                graphService.buildGraph(
                        request.getHour(),
                        request.getDayOfWeek(),
                        request.getWeather()
                );


        // -----------------------------------------
        // Dijkstra
        // -----------------------------------------

        RouteResult result =
                dijkstraAlgorithm.findShortestPath(
                        graph,
                        request.getSource(),
                        request.getDestination()
                );


        // -----------------------------------------
        // Traffic level
        // -----------------------------------------

        String trafficLevel =
                determineTrafficLevel(
                        result.getBaseTravelTime(),
                        result.getEstimatedTravelTime()
                );


        // -----------------------------------------
        // Traffic increase
        // -----------------------------------------

        double trafficIncreasePercentage =
                calculateTrafficIncrease(
                        result.getBaseTravelTime(),
                        result.getEstimatedTravelTime()
                );


        // -----------------------------------------
        // Find alternative routes
        // -----------------------------------------

        List<AlternativeRoute> alternatives =
                findAlternativeRoutes(
                        graph,
                        request.getSource(),
                        request.getDestination(),
                        result.getPath()
                );


        // -----------------------------------------
        // Return response
        // -----------------------------------------

        return new RouteResponse(

                request.getSource(),

                request.getDestination(),

                result.getPath(),

                result.getTotalDistance(),

                result.getBaseTravelTime(),

                result.getEstimatedTravelTime(),

                trafficIncreasePercentage,

                trafficLevel,

                request.getWeather(),

                alternatives
        );
    }


    // =============================================
    // Find alternative routes
    // =============================================

    private List<AlternativeRoute> findAlternativeRoutes(

            Graph graph,

            String source,

            String destination,

            List<String> bestPath

    ) {

        List<AlternativeRoute> routes =
                new ArrayList<>();


        List<List<String>> allPaths =
                new ArrayList<>();


        List<String> currentPath =
                new ArrayList<>();


        Set<String> visited =
                new HashSet<>();


        currentPath.add(source);

        visited.add(source);


        // -----------------------------------------
        // Generate simple paths
        // -----------------------------------------

        generatePaths(
                graph,
                source,
                destination,
                currentPath,
                visited,
                allPaths
        );


        // -----------------------------------------
        // Convert paths to route objects
        // -----------------------------------------

        for (List<String> path : allPaths) {

            // Don't include the recommended route
            if (path.equals(bestPath)) {
                continue;
            }


            double totalDistance = 0.0;

            double baseTravelTime = 0.0;

            double estimatedTravelTime = 0.0;


            // -------------------------------------
            // Calculate route metrics
            // -------------------------------------

            for (int i = 0;
                 i < path.size() - 1;
                 i++) {

                String current =
                        path.get(i);

                String next =
                        path.get(i + 1);


                Edge edge =
                        findEdge(
                                graph,
                                current,
                                next
                        );


                if (edge == null) {
                    continue;
                }


                totalDistance +=
                        edge.getDistance();


                baseTravelTime +=
                        edge.getBaseTravelTime();


                estimatedTravelTime +=
                        edge.getWeight();
            }


            routes.add(
                    new AlternativeRoute(
                            new ArrayList<>(path),
                            totalDistance,
                            estimatedTravelTime,
                            baseTravelTime
                    )
            );
        }


        // -----------------------------------------
        // Sort by predicted travel time
        // -----------------------------------------

        routes.sort(
                Comparator.comparingDouble(
                        AlternativeRoute::getEstimatedTravelTime
                )
        );


        // -----------------------------------------
        // Return only top 3 alternatives
        // -----------------------------------------

        if (routes.size() > 3) {

            return new ArrayList<>(
                    routes.subList(0, 3)
            );
        }


        return routes;
    }


    // =============================================
    // Generate simple paths using DFS
    // =============================================

    private void generatePaths(

            Graph graph,

            String current,

            String destination,

            List<String> currentPath,

            Set<String> visited,

            List<List<String>> allPaths

    ) {

        // -----------------------------------------
        // Destination reached
        // -----------------------------------------

        if (current.equals(destination)) {

            allPaths.add(
                    new ArrayList<>(
                            currentPath
                    )
            );

            return;
        }


        // -----------------------------------------
        // Explore neighbors
        // -----------------------------------------

        for (Edge edge :
                graph.getNeighbors(current)) {

            String next =
                    edge.getDestination();


            // Prevent cycles
            if (visited.contains(next)) {
                continue;
            }


            visited.add(next);

            currentPath.add(next);


            generatePaths(
                    graph,
                    next,
                    destination,
                    currentPath,
                    visited,
                    allPaths
            );


            // Backtrack
            currentPath.remove(
                    currentPath.size() - 1
            );

            visited.remove(next);
        }
    }


    // =============================================
    // Find edge
    // =============================================

    private Edge findEdge(

            Graph graph,

            String source,

            String destination

    ) {

        for (Edge edge :
                graph.getNeighbors(source)) {

            if (
                    edge.getDestination()
                            .equals(destination)
            ) {

                return edge;
            }
        }


        return null;
    }


    // =============================================
    // Calculate traffic increase
    // =============================================

    private double calculateTrafficIncrease(

            double baseTravelTime,

            double predictedTravelTime

    ) {

        if (baseTravelTime <= 0) {

            return 0.0;
        }


        return (
                (
                        predictedTravelTime
                                - baseTravelTime
                )
                        / baseTravelTime
        ) * 100.0;
    }


    // =============================================
    // Determine traffic level
    // =============================================

    private String determineTrafficLevel(

            double baseTravelTime,

            double predictedTravelTime

    ) {

        if (baseTravelTime <= 0) {

            return "UNKNOWN";
        }


        double increase =
                (
                        predictedTravelTime
                                - baseTravelTime
                )
                        / baseTravelTime;


        if (increase < 0.20) {

            return "LOW";
        }


        if (increase < 0.50) {

            return "MEDIUM";
        }


        return "HIGH";
    }
}