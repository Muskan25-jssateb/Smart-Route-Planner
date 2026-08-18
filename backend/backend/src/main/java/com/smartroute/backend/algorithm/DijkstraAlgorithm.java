package com.smartroute.backend.algorithm;

import com.smartroute.backend.model.Edge;
import com.smartroute.backend.model.Graph;
import com.smartroute.backend.model.RouteResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {

    private static class NodeDistance {

        String node;
        double distance;

        NodeDistance(
                String node,
                double distance
        ) {
            this.node = node;
            this.distance = distance;
        }
    }

    public RouteResult findShortestPath(
            Graph graph,
            String source,
            String destination
    ) {

        // -----------------------------------------
        // Validate source
        // -----------------------------------------

        if (!graph.containsLocation(source)) {

            throw new IllegalArgumentException(
                    "Source location does not exist: "
                            + source
            );
        }


        // -----------------------------------------
        // Validate destination
        // -----------------------------------------

        if (!graph.containsLocation(destination)) {

            throw new IllegalArgumentException(
                    "Destination location does not exist: "
                            + destination
            );
        }


        // -----------------------------------------
        // Maps
        // -----------------------------------------

        Map<String, Double> distances =
                new HashMap<>();

        Map<String, Double> totalDistances =
                new HashMap<>();

        Map<String, Double> baseTimes =
                new HashMap<>();

        Map<String, String> previous =
                new HashMap<>();


        // -----------------------------------------
        // Initialize values
        // -----------------------------------------

        for (String location :
                graph.getAdjacencyList().keySet()) {

            distances.put(
                    location,
                    Double.POSITIVE_INFINITY
            );

            totalDistances.put(
                    location,
                    0.0
            );

            baseTimes.put(
                    location,
                    0.0
            );
        }


        // -----------------------------------------
        // Source initialization
        // -----------------------------------------

        distances.put(
                source,
                0.0
        );


        // -----------------------------------------
        // Priority Queue
        // -----------------------------------------

        PriorityQueue<NodeDistance> queue =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                node -> node.distance
                        )
                );

        queue.offer(
                new NodeDistance(
                        source,
                        0.0
                )
        );


        // -----------------------------------------
        // Dijkstra
        // -----------------------------------------

        while (!queue.isEmpty()) {

            NodeDistance current =
                    queue.poll();

            String currentNode =
                    current.node;

            double currentDistance =
                    current.distance;


            // Ignore outdated queue entries
            if (currentDistance >
                    distances.get(currentNode)) {

                continue;
            }


            // Destination reached
            if (currentNode.equals(destination)) {

                break;
            }


            // -------------------------------------
            // Explore neighbors
            // -------------------------------------

            for (Edge edge :
                    graph.getNeighbors(currentNode)) {

                String neighbor =
                        edge.getDestination();


                // Predicted travel time is the
                // Dijkstra edge weight
                double newTravelTime =
                        currentDistance
                                + edge.getWeight();


                // ---------------------------------
                // Better route found
                // ---------------------------------

                if (newTravelTime <
                        distances.get(neighbor)) {

                    distances.put(
                            neighbor,
                            newTravelTime
                    );


                    previous.put(
                            neighbor,
                            currentNode
                    );


                    // Total physical distance
                    totalDistances.put(
                            neighbor,
                            totalDistances.get(currentNode)
                                    + edge.getDistance()
                    );


                    // Total normal/base travel time
                    baseTimes.put(
                            neighbor,
                            baseTimes.get(currentNode)
                                    + edge.getBaseTravelTime()
                    );


                    queue.offer(
                            new NodeDistance(
                                    neighbor,
                                    newTravelTime
                            )
                    );
                }
            }
        }


        // -----------------------------------------
        // No route
        // -----------------------------------------

        if (distances.get(destination) ==
                Double.POSITIVE_INFINITY) {

            throw new IllegalArgumentException(
                    "No route exists between "
                            + source
                            + " and "
                            + destination
            );
        }


        // -----------------------------------------
        // Reconstruct route
        // -----------------------------------------

        List<String> path =
                reconstructPath(
                        previous,
                        source,
                        destination
                );


        // -----------------------------------------
        // Return result
        // -----------------------------------------

        return new RouteResult(
                path,
                distances.get(destination),
                totalDistances.get(destination),
                baseTimes.get(destination)
        );
    }


    // =============================================
    // Reconstruct path
    // =============================================

    private List<String> reconstructPath(
            Map<String, String> previous,
            String source,
            String destination
    ) {

        List<String> path =
                new ArrayList<>();

        String current =
                destination;


        while (current != null) {

            path.add(current);

            if (current.equals(source)) {

                break;
            }

            current =
                    previous.get(current);
        }


        Collections.reverse(path);

        return path;
    }
}