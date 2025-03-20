package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkerState;
import com.adg.geomonitoringapi.event.Worker;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkerCreationEvent extends Event {
    private Worker worker;

    @Override
    public SystemState apply(SystemState oldState) {
        Long newWorkerId = getId();

        if (oldState.getWorkers().containsKey(newWorkerId))
            throw new SystemState.StateUpdateException("Невозможно создать рабочего: рабочий с id "
                    + newWorkerId + " уже существует");

        var newWorkers = new HashMap<>(oldState.getWorkers());
        newWorkers.put(getId(),
                WorkerState.builder().name(worker.getName()).id(getId()).build()
        );

        return oldState.withWorkers(newWorkers);
    }
}
