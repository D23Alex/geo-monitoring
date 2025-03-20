package com.adg.geomonitoringapi.geometry;

import com.adg.geomonitoringapi.event.Point;

import java.util.Collection;
import java.util.List;

public class Geometry {
    private static final double R = 6371.0; // Радиус Земли в километрах

    public static Double haversine(Double lat1, Double lon1, Double lat2, Double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dlat = lat2Rad - lat1Rad;
        double dlon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static Double haversine(Point p1, Point p2) {
        return haversine(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
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
}
