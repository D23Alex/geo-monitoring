package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.event.Worker;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CriteriaCompletedEventResponseDTO extends EventResponseDTO {
    private Long taskId;
    private Integer criteriaNumber;
    private Instant completedAt;
    private String completionComment;
    private Worker completedBy;
}
