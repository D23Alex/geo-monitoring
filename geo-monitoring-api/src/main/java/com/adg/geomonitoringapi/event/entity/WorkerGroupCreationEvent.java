package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.group.entity.Group;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerGroupCreationEvent extends Event {
    @ManyToOne
    private Worker foreman;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Worker> workers;
    Instant groupActiveFrom;
    Instant groupActiveTo;
    Instant timestamp;

    @Override
    public SystemState updateState(SystemState oldState) {
        System.out.println("in update state of WorkerGroupCreationEvent with id " + getId() + " and workers:");
        for (Worker worker : workers) {
            System.out.println(worker.getId() + ": " + worker.getName());
        }

        if (groupActiveFrom.isAfter(oldState.getTimestamp()))
            return new SystemState(Stream.concat(oldState.getFutureGroups().stream(), Stream.of(
                    new Group(workers, foreman)
            )).collect(Collectors.toSet()),
                    oldState.getActiveGroups(),
                    oldState.getIdleWorkers(),
                    oldState.getTasks(),
                    oldState.getTimestamp());

        if (groupActiveFrom.isBefore(oldState.getTimestamp()) && groupActiveTo.isAfter(oldState.getTimestamp()))
            return new SystemState(Stream.concat(oldState.getFutureGroups().stream(), Stream.of(
                    new Group(workers, foreman)
            )).collect(Collectors.toSet()),
                    oldState.getActiveGroups(),
                    oldState.getIdleWorkers(),
                    oldState.getTasks(),
                    oldState.getTimestamp());

        return new SystemState(null, null, null, null, null);
    }
}
