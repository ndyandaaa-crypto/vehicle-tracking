package com.example.tracking.repository;

import com.example.tracking.model.Vehicle;
import com.example.tracking.model.VehicleStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(VehicleStatus status, Sort sort);
    List<Vehicle> findByRouteId(Long routeId, Sort sort);
    Optional<Vehicle> findFirstByDriverId(Long driverId);
    long countByRouteId(Long routeId);
}
