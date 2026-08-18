package com.smartroute.backend.controller;

import com.smartroute.backend.model.Location;
import com.smartroute.backend.repository.LocationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:3000",
                "https://smart-route-frontend.onrender.com"
        },
        methods = {
                RequestMethod.GET,
                RequestMethod.OPTIONS
        }
)
public class LocationController {

    private final LocationRepository locationRepository;

    public LocationController(
            LocationRepository locationRepository
    ) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }
}