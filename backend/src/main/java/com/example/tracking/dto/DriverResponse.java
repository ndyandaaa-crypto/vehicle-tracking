package com.example.tracking.dto;

import java.time.LocalDate;

public record DriverResponse(Long id, String firstName, String lastName, String fullName,
                             String phone, String email, String licenseNumber,
                             LocalDate licenseExpiry, VehicleSummary vehicle) {
}
