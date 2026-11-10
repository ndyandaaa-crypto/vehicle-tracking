package com.example.tracking.dto;

/** Short route details shown next to a vehicle. */
public record RouteRef(Long id, String name, String origin, String destination) {
}
