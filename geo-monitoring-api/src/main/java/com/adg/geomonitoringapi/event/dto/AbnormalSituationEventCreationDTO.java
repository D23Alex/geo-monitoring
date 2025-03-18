package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.event.WorkerResponseDTO;
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
public class AbnormalSituationEventCreationDTO extends EventCreationDTO {
    private WorkerResponseDTO worker;
    private String description;
}
