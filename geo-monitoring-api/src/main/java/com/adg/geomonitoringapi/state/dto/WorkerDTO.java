package com.adg.geomonitoringapi.state.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WorkerDTO {
    private Long id;
    private String name;
    private Optional<PointDTO> lastKnownPosition;
    private Double distanceTravelled;
    private Boolean isIdle;
}
