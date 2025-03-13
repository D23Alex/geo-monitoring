package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.task.entity.Task;
import com.adg.geomonitoringapi.task.status.TaskStatus;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskAssignedEvent extends Event {
    private Long taskId;
    private Worker worker; // Работник, назначаемый на задачу

    @Override
    public SystemState updateState(SystemState oldState) {
        Set<Task> updatedTasks = oldState.getTasks().stream().map(task -> {
            if (task.getId().equals(taskId)) {
                task.getAssignedWorkers().add(worker);
                task.setStatus(TaskStatus.ASSIGNED);
            }
            return task;
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
