package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskCompletedEvent extends Event {
    private Long taskId;
    private Instant closedAt;

    @Override
    public SystemState updateState(SystemState oldState) {
        if (!oldState.getTasks().containsKey(taskId))
            throw new SystemState.StateUpdateException("Невозможно выполнить задачу: задача с id "
                    + taskId + " не существует");

        TaskState updatedTask = oldState.getTasks().get(taskId)
                .withStatus(TaskStatus.COMPLETED)
                .withClosedAt(closedAt);

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(taskId, updatedTask);

        return oldState.withTasks(newTasks);
    }
}
