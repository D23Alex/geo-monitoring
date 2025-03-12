package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.model.Group;
import com.adg.geomonitoringapi.model.Worker;

import java.time.Instant;
import java.util.Set;

public record SystemState(
        Set<Group> futureGroups,
        Set<Group> activeGroups,
        Set<Worker> idleWorkers,
        Instant timestamp
) {
    public static SystemState initial() {
        return new SystemState(Set.of(), Set.of(), Set.of(), Instant.now());
    }
}
