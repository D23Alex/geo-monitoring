package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.GroupState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.util.Interval;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Hashtable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkerGroupCreationEvent extends Event {
    private Long foremanId;
    private String name;
    @ElementCollection
    private Set<Long> workerIds;
    Instant groupActiveFrom;
    Instant groupActiveTo;

    @PrePersist
    @PreUpdate
    private void validateWorkerIds() {
        if (workerIds.contains(foremanId)) {
            throw new IllegalArgumentException("Бригадир не может быть рабочим в этой группе.");
        }
    }

    @Override
    public SystemState apply(SystemState oldState) {
        Long newGroupId = getId();

        GroupState newGroup = GroupState.builder()
                .id(newGroupId)
                .workerIds(workerIds)
                .foremanId(foremanId)
                .name(name)
                .activeInterval(new Interval(groupActiveFrom, groupActiveTo))
                .createdAt(getTimestamp())
                .build();

        if (oldState.getTasks().containsKey(newGroupId))
            throw new SystemState.StateUpdateException("Невозможно создать группу: группа с id "
                    + newGroupId + " уже существует");

        if (!oldState.getWorkers().keySet().containsAll(workerIds))
            throw new SystemState.StateUpdateException("Невозможно создать группу: заданных работников не существует");

        if (!oldState.getWorkers().containsKey(foremanId))
            throw new SystemState.StateUpdateException("Невозможно создать группу: заданного бригадира не существует");

        var newGroups = new Hashtable<>(oldState.getGroups());
        newGroups.put(newGroupId, newGroup);

        return oldState.withGroups(newGroups);
    }
}
