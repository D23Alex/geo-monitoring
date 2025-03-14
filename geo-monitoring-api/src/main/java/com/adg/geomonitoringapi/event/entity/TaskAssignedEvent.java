package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedEvent extends Event {
    private String description;
    // Работники, назначенные на задачу
    @OneToMany
    private Set<Worker> assignedWorkers;
    // Простой критерий завершения (можно расширить) TODO: реализовать критерии
    private String completionCriteria;
    private Long locationId;
    private Instant activeFrom;
    private Instant activeTo;

    @Override
    public SystemState updateState(SystemState oldState) {
        if (!oldState.getLocations().containsKey(locationId))
            throw new SystemState.StateUpdateException("Невозможно создать задачу: локация с id "
                    + locationId + " не существует");

        TaskState newTask = TaskState.builder()
                .assignedWorkers(assignedWorkers)
                .completionCriteria(completionCriteria)
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
