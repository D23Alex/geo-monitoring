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
    private Long foremanId;
    @ElementCollection //TODO: заменить во всех ивентах Worker на передачу id
    private Set<Long> workerIds;
    Instant groupActiveFrom;
    Instant groupActiveTo;

    @Override
    public SystemState apply(SystemState oldState) {
        GroupState newGroup = GroupState.builder()
                .workerIds(workerIds)
                .foremanId(foremanId)
                .activeFrom(groupActiveFrom)
                .activeTo(groupActiveTo)
                .createdAt(getTimestamp())
                .build();

        Long newGroupId = getId();
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
