package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.GroupState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Hashtable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class WorkerGroupCreationEvent extends Event {
    private Worker foreman;
    @ElementCollection
    private Set<Worker> workers;
    Instant groupActiveFrom;
    Instant groupActiveTo;
    Instant timestamp;

    @Override
    public SystemState apply(SystemState oldState) {
        GroupState newGroup = GroupState.builder()
                .workers(workers)
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
