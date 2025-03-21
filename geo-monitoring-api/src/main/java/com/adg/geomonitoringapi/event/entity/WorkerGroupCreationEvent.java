package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.GroupState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.util.Interval;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Hashtable;
import java.util.Map;
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
    @ElementCollection //TODO: заменить во всех ивентах Worker на передачу id
    private Set<Long> workerIds;
    Instant groupActiveFrom;
    Instant groupActiveTo;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> oldState.getGroups().containsKey(getId()),
                "Невозможно создать группу: группа с id " + getId() + " уже существует",
                () -> !oldState.getWorkers().keySet().containsAll(workerIds),
                "Невозможно создать группу: заданных работников не существует",
                () -> !oldState.getWorkers().containsKey(foremanId),
                "Невозможно создать группу: заданного бригадира не существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        Long newGroupId = getId();

        GroupState newGroup = GroupState.builder()
                .id(newGroupId)
                .workerIds(workerIds)
                .foremanId(foremanId)
                .activeInterval(new Interval(groupActiveFrom, groupActiveTo))
                .createdAt(getTimestamp())
                .build();

        var newGroups = new Hashtable<>(oldState.getGroups());
        newGroups.put(newGroupId, newGroup);

        return oldState.withGroups(newGroups);
    }
}
