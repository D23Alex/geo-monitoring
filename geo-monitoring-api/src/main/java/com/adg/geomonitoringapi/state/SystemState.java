package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.AbsenceReason;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.NothingHappenedEvent;
import com.adg.geomonitoringapi.worker.entity.Worker;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

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

    public static Set<Worker> idleWorkers() {
        return null; //TODO: implement
    }

    public static SystemState initial() {
        return new SystemState(Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                NothingHappenedEvent.builder().id(0L).timestamp(Instant.EPOCH).build()
        );
    }


}