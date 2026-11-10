package com.example.tracking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** A named stop on a route. Order is kept by the route's waypoint list. */
@Embeddable
public class Waypoint {

    @Column(name = "stop_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    protected Waypoint() {
        // required by JPA
    }

    public Waypoint(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
