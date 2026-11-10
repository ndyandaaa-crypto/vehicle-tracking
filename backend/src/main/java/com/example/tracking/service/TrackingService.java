package com.example.tracking.service;

import com.example.tracking.dto.*;
import com.example.tracking.exception.ResourceNotFoundException;
import com.example.tracking.model.*;
import com.example.tracking.repository.DriverRepository;
import com.example.tracking.repository.RouteRepository;
import com.example.tracking.repository.VehicleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrackingService {

    private static final Sort BY_REGISTRATION = Sort.by("registrationNumber");

    private final VehicleRepository vehicles;
    private final RouteRepository routes;
    private final DriverRepository drivers;

    public TrackingService(VehicleRepository vehicles, RouteRepository routes, DriverRepository drivers) {
        this.vehicles = vehicles;
        this.routes = routes;
        this.drivers = drivers;
    }

    // ---------- vehicles ----------

    public List<VehicleResponse> listVehicles(VehicleStatus status) {
        List<Vehicle> found = status == null
                ? vehicles.findAll(BY_REGISTRATION)
                : vehicles.findByStatus(status, BY_REGISTRATION);
        return found.stream().map(this::toVehicleResponse).toList();
    }

    public VehicleDetailResponse getVehicle(Long id) {
        Vehicle vehicle = vehicles.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        Driver driver = vehicle.getDriver();
        Route route = vehicle.getRoute();
        return new VehicleDetailResponse(
                toVehicleResponse(vehicle),
                driver == null ? null : toDriverResponse(driver),
                route == null ? null : toRouteDetail(route));
    }

    // ---------- routes ----------

    public List<RouteSummary> listRoutes() {
        return routes.findAll(Sort.by("name")).stream()
                .map(r -> new RouteSummary(r.getId(), r.getName(), r.getOrigin(), r.getDestination(),
                        r.getDistanceKm(), r.getEstimatedMinutes(), vehicles.countByRouteId(r.getId())))
                .toList();
    }

    public RouteDetailResponse getRoute(Long id) {
        Route route = routes.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route", id));
        return toRouteDetail(route);
    }

    // ---------- drivers ----------

    public List<DriverResponse> listDrivers() {
        return drivers.findAll(Sort.by("lastName", "firstName")).stream().map(this::toDriverResponse).toList();
    }

    public DriverResponse getDriver(Long id) {
        Driver driver = drivers.findById(id).orElseThrow(() -> new ResourceNotFoundException("Driver", id));
        return toDriverResponse(driver);
    }

    // ---------- mapping ----------

    private VehicleResponse toVehicleResponse(Vehicle v) {
        Driver d = v.getDriver();
        Route r = v.getRoute();
        return new VehicleResponse(v.getId(), v.getRegistrationNumber(), v.getMake(), v.getModel(),
                v.getManufactureYear(), v.getType(), v.getStatus(), v.getLatitude(), v.getLongitude(),
                v.getSpeedKph(), v.getLastUpdated(), v.getRouteProgress(),
                d == null ? null : new DriverRef(d.getId(), d.getFullName(), d.getPhone()),
                r == null ? null : new RouteRef(r.getId(), r.getName(), r.getOrigin(), r.getDestination()));
    }

    private VehicleSummary toVehicleSummary(Vehicle v) {
        return new VehicleSummary(v.getId(), v.getRegistrationNumber(), v.getMake(), v.getModel(),
                v.getType(), v.getStatus());
    }

    private DriverResponse toDriverResponse(Driver d) {
        VehicleSummary assigned = vehicles.findFirstByDriverId(d.getId()).map(this::toVehicleSummary).orElse(null);
        return new DriverResponse(d.getId(), d.getFirstName(), d.getLastName(), d.getFullName(),
                d.getPhone(), d.getEmail(), d.getLicenseNumber(), d.getLicenseExpiry(), assigned);
    }

    private RouteDetailResponse toRouteDetail(Route r) {
        List<WaypointDto> stops = r.getWaypoints().stream()
                .map(w -> new WaypointDto(w.getName(), w.getLatitude(), w.getLongitude()))
                .toList();
        List<VehicleResponse> onRoute = vehicles.findByRouteId(r.getId(), BY_REGISTRATION).stream()
                .map(this::toVehicleResponse)
                .toList();
        return new RouteDetailResponse(r.getId(), r.getName(), r.getOrigin(), r.getDestination(),
                r.getDistanceKm(), r.getEstimatedMinutes(), stops, onRoute);
    }
}
