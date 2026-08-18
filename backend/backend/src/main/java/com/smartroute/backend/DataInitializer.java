package com.smartroute.backend;

import com.smartroute.backend.model.Location;
import com.smartroute.backend.model.Road;
import com.smartroute.backend.repository.LocationRepository;
import com.smartroute.backend.repository.RoadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(
            LocationRepository locationRepository,
            RoadRepository roadRepository
    ) {
        return args -> {

            // Clear existing demo data
            roadRepository.deleteAll();
            locationRepository.deleteAll();

            // =========================
            // LOCATIONS
            // =========================

            Location A = locationRepository.save(
                    new Location(
                            "A",
                            12.9716,
                            77.5946
                    )
            );

            Location B = locationRepository.save(
                    new Location(
                            "B",
                            12.9352,
                            77.6245
                    )
            );

            Location C = locationRepository.save(
                    new Location(
                            "C",
                            12.9141,
                            77.6101
                    )
            );

            Location D = locationRepository.save(
                    new Location(
                            "D",
                            12.9611,
                            77.6387
                    )
            );

            Location E = locationRepository.save(
                    new Location(
                            "E",
                            12.9850,
                            77.6200
                    )
            );

            Location F = locationRepository.save(
                    new Location(
                            "F",
                            12.9000,
                            77.6350
                    )
            );

            Location G = locationRepository.save(
                    new Location(
                            "G",
                            12.9500,
                            77.5800
                    )
            );

            Location H = locationRepository.save(
                    new Location(
                            "H",
                            12.9250,
                            77.6500
                    )
            );


            // =========================
            // ROADS
            // =========================

            roadRepository.save(
                    new Road(A, B, 5, 7, "Main Road")
            );

            roadRepository.save(
                    new Road(A, C, 3, 6, "Residential")
            );

            roadRepository.save(
                    new Road(A, E, 6, 5, "Highway")
            );

            roadRepository.save(
                    new Road(A, G, 4, 6, "Main Road")
            );

            roadRepository.save(
                    new Road(B, D, 5, 6, "Highway")
            );

            roadRepository.save(
                    new Road(B, E, 3, 4, "Main Road")
            );

            roadRepository.save(
                    new Road(C, D, 4, 7, "Main Road")
            );

            roadRepository.save(
                    new Road(C, F, 5, 8, "Residential")
            );

            roadRepository.save(
                    new Road(D, H, 4, 5, "Highway")
            );

            roadRepository.save(
                    new Road(E, D, 3, 4, "Main Road")
            );

            roadRepository.save(
                    new Road(F, H, 4, 6, "Main Road")
            );

            roadRepository.save(
                    new Road(G, E, 5, 7, "Residential")
            );

            roadRepository.save(
                    new Road(G, C, 3, 5, "Main Road")
            );

            roadRepository.save(
                    new Road(H, D, 4, 5, "Highway")
            );
        };
    }
}