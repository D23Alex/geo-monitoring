package com.adg.geomonitoringapi.event.dto;

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
public class TaskCreatedEventCreationDTO extends AbstractEventCreationDTO {
    private String description;
    private Double latitude; 
    private Double longitude;
    private String completionCriteria;
    private Instant createdAt;
}
