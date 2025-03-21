package com.adg.geomonitoringapi.geometry;

import com.adg.geomonitoringapi.event.Point;

import java.util.Collection;
import java.util.List;

public class Geometry {
    private static final double R = 6371.0; // Радиус Земли в километрах

    public static double haversine(Point p1, Point p2) {
        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static Double totalTravelDistance(Collection<Point> travelHistory) {
        double totalDistance = 0.0;
        Point prevPoint = null;

        for (Point point : travelHistory) {
            if (prevPoint != null) {
                totalDistance += Geometry.haversine(prevPoint, point);
            }
            prevPoint = point;
        }
        return totalDistance;
    }

    public static boolean isPointInPolygon(Point point, List<Point> polygon) {
        int n = polygon.size();
        boolean inside = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point vertex1 = polygon.get(i);
            Point vertex2 = polygon.get(j);

            if (((vertex1.getLatitude() > point.getLatitude()) != (vertex2.getLatitude() > point.getLatitude())) &&
                    (point.getLongitude() < (vertex2.getLongitude() - vertex1.getLongitude())
                            * (point.getLatitude() - vertex1.getLatitude())
                            / (vertex2.getLatitude() - vertex1.getLatitude()) + vertex1.getLongitude())) {
                inside = !inside;
            }
        }
        return inside;
    }

    public static boolean isPointWithinDistanceFromPolygon(Point point, List<Point> polygon, double distance) {
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            Point vertex1 = polygon.get(i);
            Point vertex2 = polygon.get(j);

            if (distanceToSegment(point, vertex1, vertex2) <= distance) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPointInOrNearPolygon(Point point, List<Point> polygon, double distance) {
        if (isPointInPolygon(point, polygon))
            return true;

        return isPointWithinDistanceFromPolygon(point, polygon, distance);
    }

    public static double distanceToSegment(Point p, Point v, Point w) {
        double d1 = haversine(p, v);
        double d2 = haversine(p, w);
        double d3 = haversine(v, w);

        if (d3 == 0.0) return d1;
        if (d1 == 0.0 || d2 == 0.0) return 0.0;

        double angle1 = Math.acos((d1 * d1 + d3 * d3 - d2 * d2) / (2 * d1 * d3));
        double angle2 = Math.acos((d2 * d2 + d3 * d3 - d1 * d1) / (2 * d2 * d3));

        if (angle1 > Math.PI / 2) return d1;
        if (angle2 > Math.PI / 2) return d2;

        return Math.sin(angle1) * d1;
    }
}
