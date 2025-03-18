package com.adg.geomonitoringapi.event.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class EventResponseDTO extends EventDTO {
    private Long id;
}
