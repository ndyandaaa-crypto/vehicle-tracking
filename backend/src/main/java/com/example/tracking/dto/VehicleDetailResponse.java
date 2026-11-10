package com.example.tracking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A vehicle together with its driver and route")
public record VehicleDetailResponse(VehicleResponse vehicle, DriverResponse driver, RouteDetailResponse route) {
}
