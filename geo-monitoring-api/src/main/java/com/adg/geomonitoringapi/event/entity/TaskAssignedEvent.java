package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.Task;
import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
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
    @ManyToOne
    private Worker worker; // Работник, назначаемый на задачу
    private String description;
    // Работники, назначенные на задачу
    @OneToMany
    private Set<Worker> assignedWorkers = new HashSet<>();
    // Простой критерий завершения (можно расширить)
    private String completionCriteria;
    private Instant createdAt;
    private Instant closedAt;
    private Long locationCreationEventId;

    @Override
    public SystemState updateState(SystemState oldState) {
        Set<Task> updatedTasks = oldState.getTasks().stream().peek(task -> {
//            if (task.equals(ntask)) {
//                task.getAssignedWorkers().add(worker);
//                task.setStatus(TaskStatus.ASSIGNED);
//            }
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
