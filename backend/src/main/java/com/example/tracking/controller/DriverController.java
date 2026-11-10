package com.example.tracking.controller;

import com.example.tracking.dto.DriverResponse;
import com.example.tracking.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Drivers", description = "Driver details and assigned vehicles")
public class DriverController {

    private final TrackingService service;

    public DriverController(TrackingService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List drivers")
    public List<DriverResponse> list() {
        return service.listDrivers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "View one driver")
    @ApiResponse(responseCode = "404", description = "Driver not found")
    public DriverResponse get(@PathVariable Long id) {
        return service.getDriver(id);
    }
}
