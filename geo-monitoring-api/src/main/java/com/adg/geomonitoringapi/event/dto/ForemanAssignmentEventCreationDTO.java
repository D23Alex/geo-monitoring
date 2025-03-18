package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.event.Worker;
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
public class ForemanAssignmentEventCreationDTO extends EventCreationDTO {
    private Worker worker;
    private Set<Long> subordinateWorkersIds;
}
