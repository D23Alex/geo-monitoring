package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.CompletionCriteriaState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaCompletedEvent extends Event {
    private Long taskId;
    private Integer criteriaNumber;
    private Instant completedAt;
    private String completionComment;
    private Worker completedBy;

    @Override
    public SystemState apply(SystemState oldState) {
        if (!oldState.getTasks().containsKey(taskId))
            throw new SystemState.StateUpdateException("Невозможно выполнить критерий: задача с id "
                    + taskId + " не существует");

        if (!oldState.getTasks().get(taskId).getCompletionCriteria().containsKey(criteriaNumber))
            throw new SystemState.StateUpdateException("Невозможно выполнить критерий: критерия с номером "
                    + criteriaNumber + " не существует");

        CompletionCriteriaState old =  oldState.getTasks().get(taskId).getCompletionCriteria().get(criteriaNumber);

        if (old.isCompleted())
            throw new SystemState.StateUpdateException("Невозможно выполнить критерий: уже выполнено");

        CompletionCriteriaState newCompletionCriteria = old
                .withCompleted(true)
                .withComment(completionComment);

        var newCriteria = new HashMap<>(oldState.getTasks().get(taskId).getCompletionCriteria());
        newCriteria.put(criteriaNumber, newCompletionCriteria);
        TaskState updatedTask = oldState.getTasks().get(taskId).withCompletionCriteria(newCriteria);

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(taskId, updatedTask);

        return oldState.withTasks(newTasks);
    }
}
