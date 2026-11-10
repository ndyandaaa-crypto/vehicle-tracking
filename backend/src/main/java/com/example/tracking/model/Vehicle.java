package com.example.tracking.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(name = "manufacture_year", nullable = false)
    private int manufactureYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    private Double latitude;
    private Double longitude;

    @Column(name = "speed_kph")
    private Double speedKph;

    /** How far along its route the vehicle is, from 0.0 (start) to 1.0 (destination). */
    @Column(name = "route_progress", nullable = false)
    private double routeProgress;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    protected Vehicle() {
        // required by JPA
    }

    public Vehicle(String registrationNumber, String make, String model, int manufactureYear,
                   VehicleType type, VehicleStatus status, Driver driver, Route route,
                   double routeProgress, double latitude, double longitude, double speedKph,
                   Instant lastUpdated) {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.type = type;
        this.status = status;
        this.driver = driver;
        this.route = route;
        this.routeProgress = routeProgress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedKph = speedKph;
        this.lastUpdated = lastUpdated;
    }

    public void updatePosition(double latitude, double longitude, double speedKph,
                               double routeProgress, Instant at) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedKph = speedKph;
        this.routeProgress = routeProgress;
        this.lastUpdated = at;
    }

    public Long getId() { return id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getManufactureYear() { return manufactureYear; }
    public VehicleType getType() { return type; }
    public VehicleStatus getStatus() { return status; }
    public Driver getDriver() { return driver; }
    public Route getRoute() { return route; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getSpeedKph() { return speedKph; }
    public double getRouteProgress() { return routeProgress; }
    public Instant getLastUpdated() { return lastUpdated; }
}
