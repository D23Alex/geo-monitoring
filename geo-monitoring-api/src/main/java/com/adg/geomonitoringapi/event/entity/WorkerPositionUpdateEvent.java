package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkerState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.HashMap;
import java.util.TreeMap;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPositionUpdateEvent extends Event {
    private Long workerId;
    private Double latitude;
    private Double longitude;

    @Override
    public SystemState apply(SystemState oldState) {

        if (!oldState.getWorkers().containsKey(workerId))
            throw new SystemState.StateUpdateException(
                    "Невозможно обновить позицию работника: заданного работника не существует");

        WorkerState old = oldState.getWorkers().get(workerId);
        var newTravelHistory = new TreeMap<>(old.getTravelHistory());

        newTravelHistory.put(getTimestamp(), new Point(latitude, longitude));

        var newWorkers = new HashMap<>(oldState.getWorkers());
        newWorkers.put(workerId, old.withTravelHistory(newTravelHistory));

        return oldState.withWorkers(newWorkers);
    }
}
