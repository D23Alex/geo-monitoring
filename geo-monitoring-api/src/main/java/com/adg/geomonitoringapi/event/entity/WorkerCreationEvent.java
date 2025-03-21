package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkerState;
import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkerCreationEvent extends Event {
    private Worker worker;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> oldState.getWorkers().containsKey(getId()),
                "Невозможно создать рабочего: рабочий с id " + getId() + " уже существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        Long newWorkerId = getId();

        var newWorkers = new HashMap<>(oldState.getWorkers());
        newWorkers.put(newWorkerId,
                WorkerState.builder().name(worker.getName()).id(newWorkerId).build()
        );

        return oldState.withWorkers(newWorkers);
    }
}
