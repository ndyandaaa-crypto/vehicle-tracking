package com.example.tracking.dto;

import java.util.List;

public record RouteDetailResponse(Long id, String name, String origin, String destination,
                                  int distanceKm, int estimatedMinutes,
                                  List<WaypointDto> waypoints, List<VehicleResponse> vehicles) {
}
