package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.event.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TaskCancelledEvent extends Event {
    private Long taskId;
    private String closingReason;
    private Instant closedAt;

    @Override
    public SystemState updateState(SystemState oldState) {
        if (!oldState.getTasks().containsKey(taskId))
            throw new SystemState.StateUpdateException("Невозможно отменить задачу: задача с id "
                    + taskId + " не существует");

        TaskState updatedTask = oldState.getTasks().get(taskId)
                .withStatus(TaskStatus.CANCELLED)
                .withClosedAt(closedAt)
                .withClosingReason(closingReason);

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(taskId, updatedTask);

        return oldState.withTasks(newTasks);
    }
}
