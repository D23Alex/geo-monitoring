package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.util.Interval;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class TaskState {
    private String description;
    private Set<Long> assignedWorkers = new HashSet<>();
    private TaskStatus status;
    private Map<Integer, CompletionCriteriaState> completionCriteria;
    private Instant createdAt;
    private Instant closedAt;
    private String closingReason;
    private Interval activeInterval;
    private Long locationId;
}
