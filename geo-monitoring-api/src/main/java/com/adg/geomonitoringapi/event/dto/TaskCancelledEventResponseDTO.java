package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.event.TaskStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCancelledEventResponseDTO extends EventResponseDTO {
    private String closingReason;
    private TaskStatus closedStatus;
    private Instant closedAt;
}
