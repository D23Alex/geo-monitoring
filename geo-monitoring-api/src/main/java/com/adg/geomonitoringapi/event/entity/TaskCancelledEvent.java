package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.event.Task;
import com.adg.geomonitoringapi.event.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

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
    // Новый статус задачи: COMPLETED или CANCELLED
    private TaskStatus closedStatus;
    private Instant closedAt;

    @Override
    public SystemState updateState(SystemState oldState) {
        Set<Task> updatedTasks = oldState.getTasks().stream().peek(task -> {
            if (task.getId().equals(taskId)) {
                task.setStatus(closedStatus);
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
