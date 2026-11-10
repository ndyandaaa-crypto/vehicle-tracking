package com.example.tracking.dto;

import com.example.tracking.model.VehicleStatus;
import com.example.tracking.model.VehicleType;

public record VehicleSummary(Long id, String registrationNumber, String make, String model,
                             VehicleType type, VehicleStatus status) {
}
