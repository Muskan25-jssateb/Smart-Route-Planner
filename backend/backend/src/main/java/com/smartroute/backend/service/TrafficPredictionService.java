package com.smartroute.backend.service;

import com.smartroute.backend.model.TrafficPredictionRequest;
import com.smartroute.backend.model.TrafficPredictionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TrafficPredictionService {

    private final RestClient restClient;

    public TrafficPredictionService() {

        String mlServiceUrl =
                System.getenv().getOrDefault(
                        "ML_SERVICE_URL",
                        "http://127.0.0.1:8000"
                );

        this.restClient = RestClient.builder()
                .baseUrl(mlServiceUrl)
                .build();
    }

    public double predictTravelTime(
            TrafficPredictionRequest request
    ) {

        TrafficPredictionResponse response =
                restClient.post()
                        .uri("/predict")
                        .body(request)
                        .retrieve()
                        .body(TrafficPredictionResponse.class);

        if (response == null) {

            throw new RuntimeException(
                    "Traffic prediction service returned no response"
            );
        }

        return response.getPredictedTravelTime();
    }
}