package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.task.entity.Task;
import com.adg.geomonitoringapi.task.status.TaskStatus;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TaskCreatedEvent extends Event {
    private Long taskId;
    private String description;
    private Double latitude; // опционально
    private Double longitude;
    private String completionCriteria;
    private Instant createdAt;

    @Override
    public SystemState updateState(SystemState oldState) {
        Task task = new Task();
        task.setId(taskId);
        task.setDescription(description);
        task.setLatitude(latitude);
        task.setLongitude(longitude);
        task.setCompletionCriteria(completionCriteria);
        task.setStatus(TaskStatus.CREATED);
        task.setCreatedAt(createdAt != null ? createdAt : getTimestamp());

        // Создаем новый набор задач с добавлением новой задачи
        HashSet<Task> newTasks = new HashSet<>(oldState.getTasks());
        newTasks.add(task);

        // Возвращаем обновленное состояние, оставляя остальные поля без изменений
        return new SystemState(
                oldState.getFutureGroups(),
                oldState.getActiveGroups(),
                oldState.getIdleWorkers(),
                newTasks,
                oldState.getTimestamp()
        );
    }
}
