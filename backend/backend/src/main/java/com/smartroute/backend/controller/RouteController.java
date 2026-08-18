package com.smartroute.backend.controller;

import com.smartroute.backend.model.RouteRequest;
import com.smartroute.backend.model.RouteResponse;
import com.smartroute.backend.service.RouteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public RouteResponse findRoute(
            @RequestBody RouteRequest request
    ) {

        return routeService.findRoute(request);
    }
}