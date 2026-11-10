package com.example.tracking.controller;

import com.example.tracking.dto.VehicleDetailResponse;
import com.example.tracking.dto.VehicleResponse;
import com.example.tracking.model.VehicleStatus;
import com.example.tracking.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Vehicles being tracked and where they are now")
public class VehicleController {

    private final TrackingService service;

    public VehicleController(TrackingService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List tracked vehicles with their latest position")
    public List<VehicleResponse> list(
            @Parameter(description = "Only return vehicles with this status") @RequestParam(required = false) VehicleStatus status) {
        return service.listVehicles(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "View one vehicle with its driver and route")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public VehicleDetailResponse get(@PathVariable Long id) {
        return service.getVehicle(id);
    }
}
