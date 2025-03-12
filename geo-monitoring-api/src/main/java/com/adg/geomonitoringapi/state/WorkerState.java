package com.adg.geomonitoringapi.state;

import java.time.Instant;

public record WorkerState(
        Long workerId,
        double lastLatitude,
        double lastLongitude,
        double distanceTraveledToday,
        boolean isIdle,
        boolean isOutsideAssignedArea,
        Instant lastUpdated
) {
    public static WorkerState initial(Long workerId) {
        return new WorkerState(workerId, 0, 0, 0, true, false, Instant.EPOCH);
    }
}
