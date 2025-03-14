package com.adg.geomonitoringapi.event;

import com.adg.geomonitoringapi.worker.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class SystemState {
    private Set<Group> futureGroups;
    private Set<Group> activeGroups;
    private Set<Worker> idleWorkers;
    private Set<Task> tasks;
    private Instant timestamp;

    public static SystemState initial() {
        return new SystemState(Set.of(), Set.of(), Set.of(), Set.of(), Instant.now());
    }
}