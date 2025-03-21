package com.adg.geomonitoringapi.state.dto;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.state.CompletionCriteriaState;
import com.adg.geomonitoringapi.util.Interval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@With
public class TaskDTO {
    private Long id;
    private String description;
    private Set<Long> assignedWorkers;
    private TaskStatus status;
    private Map<Integer, CompletionCriteriaState> completionCriteria;
    private Instant createdAt;
    private Instant closedAt;
    private String closingReason;
    private Interval activeInterval;
    private LocationDTO locationDTO;
}
