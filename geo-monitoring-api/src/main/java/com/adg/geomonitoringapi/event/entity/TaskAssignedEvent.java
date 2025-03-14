package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.task.entity.Task;
import com.adg.geomonitoringapi.task.status.TaskStatus;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TaskAssignedEvent extends Event {
    private Long taskId;
    @ManyToOne
    private Worker worker; // Работник, назначаемый на задачу

    @Override
    public SystemState updateState(SystemState oldState) {
        Set<Task> updatedTasks = oldState.getTasks().stream().peek(task -> {
            if (task.getId().equals(taskId)) {
                task.getAssignedWorkers().add(worker);
                task.setStatus(TaskStatus.ASSIGNED);
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
