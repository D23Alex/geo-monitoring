package com.adg.geomonitoringapi.state;


import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.geometry.Geometry;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public final class WorkerState {
    private static final long IDLE_TIMEOUT_IN_SECONDS = 600L;
    private static final long IDLE_THRESHOLD_IN_METERS = 10L;

    private String name;
    @Builder.Default
    TreeMap<Instant, Point> travelHistory = new TreeMap<>();

    public WorkerState(Worker worker) {
        this.name = worker.getName();
    }

    public Worker toWorker() {
        return Worker.builder().name(name).build();
    }

    public Point lastKnownPosition() {
        return travelHistory.get(travelHistory.lastKey());
    }

    public Double distanceTravelled() {
        return Geometry.totalTravelDistance(travelHistory.values());
    }

    public Double distanceTravelledBetween(Instant t1, Instant t2) {
        NavigableMap<Instant, Point> subMap = travelHistory.subMap(t1, true, t2, true);
        if (subMap.isEmpty())
            return 0.0;

        return Geometry.totalTravelDistance(subMap.values());
    }

    public Boolean isIdle() {
        Instant now = Instant.now();
        Instant thresholdTime = now.minusSeconds(IDLE_TIMEOUT_IN_SECONDS);

        NavigableMap<Instant, Point> recentPoints = travelHistory.tailMap(thresholdTime, true);

        if (recentPoints.isEmpty())
            return true;

        Point first = recentPoints.firstEntry().getValue();
        Point last = recentPoints.lastEntry().getValue();

        return Geometry.haversine(first, last)
                < IDLE_THRESHOLD_IN_METERS;
    }

    private Double calculateDistance(NavigableMap<Instant, Point> travelHistory) {
        double totalDistance = 0.0;
        Point prevPoint = null;

        for (Point point : travelHistory.values()) {
            if (prevPoint != null) {
                totalDistance += Geometry.haversine(prevPoint, point);
            }
            prevPoint = point;
        }
        return totalDistance;
    }

    private Instant lastHeardFrom() {
        return travelHistory.lastKey();
    }
}
