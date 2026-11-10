package com.example.tracking.service;

import com.example.tracking.model.Route;
import com.example.tracking.model.Vehicle;
import com.example.tracking.model.VehicleStatus;
import com.example.tracking.repository.VehicleRepository;
import com.example.tracking.util.RouteGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Stands in for real GPS devices. Every few seconds each MOVING vehicle advances along its route,
 * as if five minutes of driving had passed. At the destination it starts the route again.
 */
@Service
@ConditionalOnProperty(name = "tracking.simulation.enabled", havingValue = "true", matchIfMissing = true)
public class SimulationService {

    private static final double SIMULATED_HOURS_PER_TICK = 5.0 / 60.0;

    private final VehicleRepository vehicles;

    public SimulationService(VehicleRepository vehicles) {
        this.vehicles = vehicles;
    }

    @Scheduled(fixedRate = 3000, initialDelay = 5000)
    @Transactional
    public void advanceVehicles() {
        for (Vehicle vehicle : vehicles.findAll()) {
            Route route = vehicle.getRoute();
            if (vehicle.getStatus() != VehicleStatus.MOVING || route == null) {
                continue;
            }
            double previousSpeed = vehicle.getSpeedKph() == null ? 70 : vehicle.getSpeedKph();
            double speed = Math.max(55, Math.min(95, previousSpeed + ThreadLocalRandom.current().nextDouble(-6, 6)));

            double progress = vehicle.getRouteProgress() + speed * SIMULATED_HOURS_PER_TICK / route.getDistanceKm();
            if (progress >= 1.0) {
                progress = 0.0;
            }
            double[] position = RouteGeometry.positionAt(route.getWaypoints(), progress);
            vehicle.updatePosition(position[0], position[1], Math.round(speed * 10) / 10.0, progress, Instant.now());
        }
    }
}
