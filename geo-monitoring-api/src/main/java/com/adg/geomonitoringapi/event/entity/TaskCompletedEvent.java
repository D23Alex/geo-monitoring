package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.event.Task;
import com.adg.geomonitoringapi.event.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskCompletedEvent extends Event {
    private Long taskId;
    private String closingReason;
    // Новый статус задачи: COMPLETED или CANCELLED
    private Instant closedAt;

    @Override
    public SystemState updateState(SystemState oldState) {
        Set<Task> updatedTasks = oldState.getTasks().stream().peek(task -> {
            if (task.getId().equals(taskId)) {
                task.setStatus(TaskStatus.COMPLETED);
                task.setClosingReason(closingReason);
                task.setClosedAt(closedAt != null ? closedAt : getTimestamp());
            }
        }).collect(Collectors.toSet());
        return new SystemState(
                oldState.getFutureGroups(),
                oldState.getActiveGroups(),
                oldState.getIdleWorkers(),
                updatedTasks,
                oldState.getTimestamp()
        );
    }
}
