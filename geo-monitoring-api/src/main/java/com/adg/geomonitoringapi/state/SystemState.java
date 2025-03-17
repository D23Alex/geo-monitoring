package com.adg.geomonitoringapi.state;

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
    private Instant timestamp;

    public static Set<Worker> idleWorkers() {
        return null; //TODO: implement
    }

    public static SystemState initial() {
      return new SystemState(Map.of(), Map.of(), Map.of(), Instant.now()
    }


}