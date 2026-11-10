package com.example.tracking.util;

import com.example.tracking.model.Waypoint;

import java.util.List;

/** Works out where a vehicle is when it has travelled a given fraction of a route. */
public final class RouteGeometry {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private RouteGeometry() {
    }

    /** @return {latitude, longitude} at {@code progress} (0.0 to 1.0) along the waypoints. */
    public static double[] positionAt(List<Waypoint> waypoints, double progress) {
        if (waypoints.isEmpty()) {
            throw new IllegalArgumentException("A route needs at least one waypoint");
        }
        if (waypoints.size() == 1) {
            return new double[]{waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude()};
        }

        double clamped = Math.max(0.0, Math.min(1.0, progress));
        double[] segment = new double[waypoints.size() - 1];
        double total = 0;
        for (int i = 0; i < segment.length; i++) {
            segment[i] = distanceKm(waypoints.get(i), waypoints.get(i + 1));
            total += segment[i];
        }

        double target = clamped * total;
        for (int i = 0; i < segment.length; i++) {
            if (target <= segment[i] || i == segment.length - 1) {
                double fraction = segment[i] == 0 ? 0 : Math.min(1.0, target / segment[i]);
                Waypoint from = waypoints.get(i);
                Waypoint to = waypoints.get(i + 1);
                return new double[]{
                        from.getLatitude() + (to.getLatitude() - from.getLatitude()) * fraction,
                        from.getLongitude() + (to.getLongitude() - from.getLongitude()) * fraction
                };
            }
            target -= segment[i];
        }
        Waypoint last = waypoints.get(waypoints.size() - 1);
        return new double[]{last.getLatitude(), last.getLongitude()};
    }

    private static double distanceKm(Waypoint a, Waypoint b) {
        double dLat = Math.toRadians(b.getLatitude() - a.getLatitude());
        double dLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(a.getLatitude())) * Math.cos(Math.toRadians(b.getLatitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(h));
    }
}
