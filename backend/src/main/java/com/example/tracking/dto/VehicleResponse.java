package com.example.tracking.dto;

import com.example.tracking.model.VehicleStatus;
import com.example.tracking.model.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "A tracked vehicle with its latest position")
public record VehicleResponse(
        Long id,
        String registrationNumber,
        String make,
        String model,
        int manufactureYear,
        VehicleType type,
        VehicleStatus status,
        Double latitude,
        Double longitude,
        Double speedKph,
        Instant lastUpdated,
        @Schema(description = "Fraction of the route completed, 0.0 to 1.0") double routeProgress,
        DriverRef driver,
        RouteRef route
) {
}
