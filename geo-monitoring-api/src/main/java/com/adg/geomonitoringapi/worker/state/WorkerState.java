package com.adg.geomonitoringapi.worker.state;

import com.adg.geomonitoringapi.location.entity.Point;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class WorkerState {
    private static final long IDLE_TIMEOUT_IN_SECONDS = 600L;
    private static final long IDLE_THRESHOLD_IN_METERS = 10L;

    //todo: сделать атрибутами заданные работнику

    TreeMap<Instant, Point> travelHistory = new TreeMap<>();

    public Point lastKnownPosition() {
        return travelHistory.get(travelHistory.lastKey());
    }

    public Double distanceTravelled() {
        return null; //todo: не делать, сначала изменить point
    }

    public Double distanceTravelledBetween(Instant t1, Instant t2) {
        return null;
    }

    public Boolean isIdle() {
        Instant now = Instant.now();
        Instant thresholdTime = now.minusSeconds(IDLE_TIMEOUT_IN_SECONDS);

        NavigableMap<Instant, Point> recentPoints = travelHistory.tailMap(thresholdTime, true);

        if (recentPoints.isEmpty())
            return true;

        Point first = recentPoints.firstEntry().getValue();
        Point last = recentPoints.lastEntry().getValue();

        // todo: вызови функцию из геометрии
        return false;
    }

    private Instant lastHeardFrom() {
        return travelHistory.lastKey();
    }
}
