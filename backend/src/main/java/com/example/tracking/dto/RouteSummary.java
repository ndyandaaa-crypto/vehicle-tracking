package com.example.tracking.dto;

public record RouteSummary(Long id, String name, String origin, String destination,
                           int distanceKm, int estimatedMinutes, long vehicleCount) {
}
