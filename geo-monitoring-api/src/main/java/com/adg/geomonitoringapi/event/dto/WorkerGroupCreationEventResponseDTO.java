package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkerGroupCreationEventResponseDTO extends EventResponseDTO {
    private WorkerResponseDTO foreman;
    private Set<WorkerResponseDTO> workers;
    Instant groupActiveFrom;
    Instant groupActiveTo;
}
