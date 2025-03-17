package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.geometry.Geometry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GeometryTest {
    @Test
    public void testHaversine() {
        assertEquals(0, Geometry.haversine(new Point(60.1, 50.1), new Point(60.1, 50.1)));
        assertEquals(37.2395, Geometry.haversine(new Point(60.1, 50.1), new Point(60.4, 50.4)), 0.001);
    }

    @Test
    public void testTotalTravelDistance() {
        assertEquals(
                Geometry.haversine(new Point(60.1, 50.1), new Point(60.2, 50.2))
                + Geometry.haversine(new Point(60.2, 50.2), new Point(60.3, 50.3))
                + Geometry.haversine(new Point(60.3, 50.3), new Point(60.4, 50.4)),
                Geometry.totalTravelDistance(
                        List.of(new Point(60.1, 50.1),
                                new Point(60.2, 50.2),
                                new Point(60.3, 50.3),
                                new Point(60.4, 50.4))
                )
        );
    }
}
