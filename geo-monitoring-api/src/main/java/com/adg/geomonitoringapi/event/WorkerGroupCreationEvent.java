package com.adg.geomonitoringapi.event;

import com.adg.geomonitoringapi.model.Group;
import com.adg.geomonitoringapi.model.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class WorkerGroupCreationEvent extends Event {
    @Id
    private Long id;
    @ManyToOne
    private Worker brigadier;
    @OneToMany
    private Set<Worker> workers;
    Instant groupActiveFrom;
    Instant groupActiveTo;
    Instant timestamp;

    @Override
    public SystemState updateState(SystemState oldState) {
        if (groupActiveFrom.isAfter(oldState.timestamp()))
            return new SystemState(Stream.concat(oldState.futureGroups().stream(), Stream.of(
                    new Group(workers, brigadier)
            )).collect(Collectors.toSet()),
                    oldState.activeGroups(),
                    oldState.idleWorkers(),
                    oldState.timestamp());

        if (groupActiveFrom.isBefore(oldState.timestamp()) && groupActiveTo.isAfter(oldState.timestamp()))
            return new SystemState(Stream.concat(oldState.futureGroups().stream(), Stream.of(
                    new Group(workers, brigadier)
            )).collect(Collectors.toSet()),
                    oldState.activeGroups(),
                    oldState.idleWorkers(),
                    oldState.timestamp());

        return new SystemState(null, null, null, null);
    }
}
