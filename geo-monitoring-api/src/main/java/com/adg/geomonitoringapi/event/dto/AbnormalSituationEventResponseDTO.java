package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AbnormalSituationEventResponseDTO extends AbstractEventResponseDTO{
    private WorkerResponseDTO worker;
    private String description;
}
