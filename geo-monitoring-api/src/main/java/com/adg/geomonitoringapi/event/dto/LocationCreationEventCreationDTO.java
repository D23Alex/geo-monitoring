package com.adg.geomonitoringapi.event.dto;

import com.adg.geomonitoringapi.event.Point;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationCreationEventCreationDTO extends EventCreationDTO {
    private String name;
    private Set<Point> points;
}
