package com.example.tracking.controller;

import com.example.tracking.dto.RouteDetailResponse;
import com.example.tracking.dto.RouteSummary;
import com.example.tracking.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@Tag(name = "Routes", description = "Routes, their stops and the vehicles driving them")
public class RouteController {

    private final TrackingService service;

    public RouteController(TrackingService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List routes")
    public List<RouteSummary> list() {
        return service.listRoutes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "View one route with its stops and the vehicles on it")
    @ApiResponse(responseCode = "404", description = "Route not found")
    public RouteDetailResponse get(@PathVariable Long id) {
        return service.getRoute(id);
    }
}
