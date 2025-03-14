package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.worker.entity.Worker;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class TaskState {
    private String description;
    // Работники, назначенные на задачу
    private Set<Worker> assignedWorkers = new HashSet<>();
    private TaskStatus status;
    // Простой критерий завершения (можно расширить)
    private String completionCriteria;
    private Instant createdAt;
    private Instant closedAt;
    private String closingReason;


}
