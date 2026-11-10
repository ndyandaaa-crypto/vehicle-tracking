package com.example.tracking.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(name = "distance_km", nullable = false)
    private int distanceKm;

    @Column(name = "estimated_minutes", nullable = false)
    private int estimatedMinutes;

    @ElementCollection
    @CollectionTable(name = "route_waypoints", joinColumns = @JoinColumn(name = "route_id"))
    @OrderColumn(name = "waypoint_order")
    private List<Waypoint> waypoints = new ArrayList<>();

    protected Route() {
        // required by JPA
    }

    public Route(String name, String origin, String destination, int distanceKm,
                 int estimatedMinutes, List<Waypoint> waypoints) {
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.estimatedMinutes = estimatedMinutes;
        this.waypoints = new ArrayList<>(waypoints);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public int getDistanceKm() { return distanceKm; }
    public int getEstimatedMinutes() { return estimatedMinutes; }
    public List<Waypoint> getWaypoints() { return waypoints; }
}
