package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkerState;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getWorkers().containsKey(workerId),
                "Невозможно обновить позицию работника: работник с id " + workerId + " не существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        WorkerState old = oldState.getWorkers().get(workerId);
        var newTravelHistory = new TreeMap<>(old.getTravelHistory());

        newTravelHistory.put(getTimestamp(), new Point(latitude, longitude));

        var newWorkers = new HashMap<>(oldState.getWorkers());
        newWorkers.put(workerId, old.withTravelHistory(newTravelHistory));

        return oldState.withWorkers(newWorkers);
    }
}
