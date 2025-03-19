package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.GroupState;
import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkerState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Hashtable;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkerGroupCreationEvent extends Event {
    private Worker foreman;
    @ElementCollection //TODO: заменить во всех ивентах Worker на передачу id
    private Set<Worker> workers;
    Instant groupActiveFrom;
    Instant groupActiveTo;

    @Override
    public SystemState apply(SystemState oldState) {
        GroupState newGroup = GroupState.builder()
                .workerIds(workers.stream().map(WorkerState::new).collect(Collectors.toSet()))
                .foreman(foreman)
                .activeFrom(groupActiveFrom)
                .activeTo(groupActiveTo)
                .createdAt(getTimestamp())
                .build();

        Long newGroupId = getId();
        if (oldState.getTasks().containsKey(newGroupId))
            throw new SystemState.StateUpdateException("Невозможно создать группу: группа с id "
                    + newGroupId + " уже существует");

        var newGroups = new Hashtable<>(oldState.getGroups());
        newGroups.put(newGroupId, newGroup);

        return oldState.withGroups(newGroups);
    }
}
