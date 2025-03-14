package com.adg.geomonitoringapi.event.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskAssignedEventCreationDTO extends EventCreationDTO {
    private Long workerId;
    private String description;
    private Set<Long> assignedWorkerIds;
    private String completionCriteria;
    private Long locationCreationEventId;
}
