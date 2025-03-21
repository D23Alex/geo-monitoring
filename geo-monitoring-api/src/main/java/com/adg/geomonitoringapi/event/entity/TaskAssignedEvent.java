package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.CompletionCriteria;
import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.state.CompletionCriteriaState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.util.Interval;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedEvent extends Event {
    private String description;
    @ElementCollection
    private Set<Long> assignedWorkers;
    @ElementCollection
    private List<CompletionCriteria> completionCriteria;
    private Long locationId;
    private Instant activeFrom;
    private Instant activeTo;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getLocations().containsKey(locationId),
                "Невозможно создать задачу: локация с id " + locationId + " не существует",
                () -> !oldState.getWorkers().keySet().containsAll(assignedWorkers),
                "Невозможно создать задачу: работник не найден",
                () -> oldState.getTasks().containsKey(getId()),
                "Невозможно создать задачу: задача с id " + getId() + " уже существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        Long newTaskId = getId();

        Map<Integer, CompletionCriteriaState> completionCriteriaStates = new HashMap<>();
        for (int i = 0; i < completionCriteria.size(); i++) {
            CompletionCriteria cur = completionCriteria.get(i);
            completionCriteriaStates.put(i,
                    CompletionCriteriaState.builder()
                            .isCompleted(false)
                            .name(cur.getName())
                            .description(cur.getDescription())
                            .isCommentRequired(cur.isCommentRequired())
                            .isPhotoProofRequired(cur.isPhotoProofRequired()).build()
            );
        }

        TaskState newTask = TaskState.builder()
                .id(newTaskId)
                .assignedWorkers(assignedWorkers)
                .locationId(locationId)
                .completionCriteria(completionCriteriaStates)
                .createdAt(getTimestamp())
                .activeInterval(new Interval(activeFrom, activeTo))
                .description(description)
                .status(TaskStatus.CREATED)
                .build();

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(newTaskId, newTask);

        return oldState.withTasks(newTasks);
    }
}
