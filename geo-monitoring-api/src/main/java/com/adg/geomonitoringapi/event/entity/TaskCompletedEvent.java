package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCompletedEvent extends Event {
    private Long taskId;
    private Instant closedAt;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getTasks().containsKey(taskId),
                "Невозможно выполнить задачу: задача с id " + taskId + " не существует",
                () -> oldState.getTasks().get(taskId).getStatus() == TaskStatus.COMPLETED,
                "Невозможно выполнить задачу: задача уже выполнена",
                () -> oldState.getTasks().get(taskId).getStatus() == TaskStatus.CANCELLED,
                "Невозможно выполнить задачу: задача уже отменена"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        TaskState updatedTask = oldState.getTasks().get(taskId)
                .withStatus(TaskStatus.COMPLETED)
                .withClosedAt(closedAt);

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(taskId, updatedTask);

        return oldState.withTasks(newTasks);
    }
}
