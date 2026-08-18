package com.smartroute.backend.controller;

import com.smartroute.backend.model.RouteRequest;
import com.smartroute.backend.model.RouteResponse;
import com.smartroute.backend.service.RouteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:3000",
                "https://smart-route-frontend.onrender.com"
        },
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
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