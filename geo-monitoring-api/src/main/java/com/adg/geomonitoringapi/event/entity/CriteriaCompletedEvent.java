package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.CompletionCriteriaState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private Long completedBy;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getTasks().containsKey(taskId),
                "Невозможно выполнить критерий: задача с id " + taskId + " не существует",
                () -> !oldState.getTasks().get(taskId).getCompletionCriteria().containsKey(criteriaNumber),
                "Невозможно выполнить критерий: критерия с номером " + criteriaNumber + " не существует",
                () -> oldState.getTasks().get(taskId).getCompletionCriteria().get(criteriaNumber).isCompleted(),
                "Невозможно выполнить критерий: уже выполнено"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        CompletionCriteriaState newCompletionCriteria = oldState
                .getTasks().get(taskId)
                .getCompletionCriteria().get(criteriaNumber)
                .withCompleted(true)
                .withComment(completionComment)
                .withCompletedBy(completedBy);

        var newCriteria = new HashMap<>(oldState.getTasks().get(taskId).getCompletionCriteria());
        newCriteria.put(criteriaNumber, newCompletionCriteria);
        TaskState updatedTask = oldState.getTasks().get(taskId).withCompletionCriteria(newCriteria);

        var newTasks = new HashMap<>(oldState.getTasks());
        newTasks.put(taskId, updatedTask);

        return oldState.withTasks(newTasks);
    }
}
