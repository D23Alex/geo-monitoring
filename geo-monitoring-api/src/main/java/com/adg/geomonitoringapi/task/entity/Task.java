package com.adg.geomonitoringapi.task.entity;

import com.adg.geomonitoringapi.task.status.TaskStatus;
import com.adg.geomonitoringapi.worker.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String description;
    // Опциональная геолокация
    private Double latitude;
    private Double longitude;
    // Работники, назначенные на задачу
    private Set<Worker> assignedWorkers = new HashSet<>();
    private TaskStatus status;
    // Простой критерий завершения (можно расширить)
    private String completionCriteria;
    private Instant createdAt;
    private Instant closedAt;
    private String closingReason;
}
