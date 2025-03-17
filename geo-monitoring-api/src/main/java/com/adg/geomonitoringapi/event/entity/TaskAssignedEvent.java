package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.CompletionCriteria;
import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.state.CompletionCriteriaState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedEvent extends Event {
    private String description;
    @ElementCollection
    private Set<Worker> assignedWorkers;
    @ElementCollection
    private List<CompletionCriteria> completionCriteria;
    private Long locationId;
    private Instant activeFrom;
    private Instant activeTo;

    @Override
    public SystemState apply(SystemState oldState) {
        if (!oldState.getLocations().containsKey(locationId))
            throw new SystemState.StateUpdateException("Невозможно создать задачу: локация с id "
                    + locationId + " не существует");

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
                .assignedWorkers(assignedWorkers)
                .completionCriteria(completionCriteriaStates)
                .createdAt(getTimestamp())
                .description(description)
                .status(TaskStatus.CREATED)
                .build();

        Long newTaskId = getId();

        if (oldState.getTasks().containsKey(newTaskId))
            throw new SystemState.StateUpdateException("Невозможно создать задачу: задача с id "
                    + newTaskId + " уже существует");

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(newTaskId, newTask);

        return oldState.withTasks(newTasks);
    }
}
