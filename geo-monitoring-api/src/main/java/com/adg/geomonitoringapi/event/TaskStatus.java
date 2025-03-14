package com.adg.geomonitoringapi.event;

import com.fasterxml.jackson.annotation.JsonFormat;

public enum TaskStatus {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    CREATED,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    ASSIGNED,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    COMPLETED,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    CANCELLED,
}
