package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.NothingHappenedEvent;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public final class SystemState {

    public static class StateUpdateException extends RuntimeException {
        public StateUpdateException(String s) {
        }
    }

    private Map<Long, GroupState> groups;
    private Map<Long, LocationState> locations;
    private Map<Long, TaskState> tasks;
    private Map<Long, WorkerState> workers;
    private Map<Long, WorkAbsenceState> absences;
    private Event lastEvent;
    private Long eventsApplied = 0L;

    public Set<Worker> idleWorkers() {
        return workers.values().stream()
                .filter(WorkerState::isIdle)
                .map(WorkerState::toWorker).collect(Collectors.toSet());
    }

    public static SystemState initial() {
        return new SystemState(Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                NothingHappenedEvent.builder().id(0L).timestamp(Instant.EPOCH).build(),
                0L
        );
    }


}