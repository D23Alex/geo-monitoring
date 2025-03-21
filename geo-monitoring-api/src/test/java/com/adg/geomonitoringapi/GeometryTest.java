package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.geometry.Geometry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.adg.geomonitoringapi.geometry.Geometry.haversine;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class GeometryTest {
    @Test
    public void testHaversine() {
        assertEquals(0, haversine(new Point(60.1, 50.1), new Point(60.1, 50.1)));
        assertEquals(37.2395, haversine(new Point(60.1, 50.1), new Point(60.4, 50.4)), 0.001);
    }

    @Test
    public void testTotalTravelDistance() {
        assertEquals(
                haversine(new Point(60.1, 50.1), new Point(60.2, 50.2))
                + haversine(new Point(60.2, 50.2), new Point(60.3, 50.3))
                + haversine(new Point(60.3, 50.3), new Point(60.4, 50.4)),
                Geometry.totalTravelDistance(
                        List.of(new Point(60.1, 50.1),
                                new Point(60.2, 50.2),
                                new Point(60.3, 50.3),
                                new Point(60.4, 50.4))
                )
        );
    }

    @Test
    public void testDistanceToSegment() {
        Point p = new Point(5.0, 5.0);
        Point v1 = new Point(0.0, 0.0);
        Point v2 = new Point(10.0, 0.0);

        // Point directly above the segment
        assertEquals(553.1495, Geometry.distanceToSegment(p, v1, v2), 0.001);

        // Point on the segment
        Point onSegment = new Point(5.0, 0.0);
        assertEquals(0.0, Geometry.distanceToSegment(onSegment, v1, v2), 0.001);

        // Point at the vertex
        assertEquals(0.0, Geometry.distanceToSegment(v1, v1, v2), 0.001);
        assertEquals(0.0, Geometry.distanceToSegment(v2, v1, v2), 0.001);

        Point p3 = new Point(11.0, 0.0);

        // Point on the line extended from the segment
        assertEquals(haversine(v2, p3), Geometry.distanceToSegment(p3, v1, v2), 0.001);
    }

    @Test
    public void testIsPointInPolygon() {
        List<Point> polygon = List.of(
                new Point(0.0, 0.0),
                new Point(0.0, 10.0),
                new Point(10.0, 10.0),
                new Point(10.0, 0.0)
        );

        // Point inside the polygon
        Point insidePoint = new Point(5.0, 5.0);
        assertTrue(Geometry.isPointInPolygon(insidePoint, polygon));

        // Point outside the polygon
        Point outsidePoint = new Point(15.0, 5.0);
        assertFalse(Geometry.isPointInPolygon(outsidePoint, polygon));

        // Point on the edge of the polygon
        Point edgePoint = new Point(0.0, 5.0);
        assertTrue(Geometry.isPointInPolygon(edgePoint, polygon));

        // Point on the vertex of the polygon
        Point vertexPoint = new Point(0.0, 0.0);
        assertTrue(Geometry.isPointInPolygon(vertexPoint, polygon));
    }

    @Test
    public void testIsPointWithinDistanceFromPolygon() {
        List<Point> polygon = List.of(
                new Point(0.0, 0.0),
                new Point(0.0, 10.0),
                new Point(10.0, 10.0),
                new Point(10.0, 0.0)
        );

        // Point within distance from the polygon
        Point nearPoint = new Point(5.0, 11.0);
        double distanceToPolygon = Geometry.distanceToSegment(nearPoint, new Point(0.0, 10.0), new Point(10.0, 10.0));
        assertTrue(Geometry.isPointWithinDistanceFromPolygon(nearPoint, polygon, distanceToPolygon + 1.0));

        // Point outside the distance from the polygon
        Point farPoint = new Point(5.0, 12.0);
        distanceToPolygon = Geometry.distanceToSegment(farPoint, new Point(0.0, 10.0), new Point(10.0, 10.0));
        assertFalse(Geometry.isPointWithinDistanceFromPolygon(farPoint, polygon, distanceToPolygon - 1.0));
    }

    @Test
    public void testIsPointInOrNearPolygon() {
        List<Point> polygon = List.of(
                new Point(0.0, 0.0),
                new Point(0.0, 10.0),
                new Point(10.0, 10.0),
                new Point(10.0, 0.0)
        );

        // Point inside the polygon
        Point insidePoint = new Point(5.0, 5.0);
        assertTrue(Geometry.isPointInOrNearPolygon(insidePoint, polygon, 0.0));

        // Point within distance from the polygon
        Point nearPoint = new Point(5.0, 11.0);
        double distanceToPolygon = Geometry.distanceToSegment(nearPoint, new Point(0.0, 10.0), new Point(10.0, 10.0));
        assertTrue(Geometry.isPointInOrNearPolygon(nearPoint, polygon, distanceToPolygon + 1.0));

        // Point outside the distance from the polygon
        Point farPoint = new Point(5.0, 12.0);
        distanceToPolygon = Geometry.distanceToSegment(farPoint, new Point(0.0, 10.0), new Point(10.0, 10.0));
        assertFalse(Geometry.isPointInOrNearPolygon(farPoint, polygon, distanceToPolygon - 1.0));
    }
}
