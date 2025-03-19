package com.adg.geomonitoringapi.event.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TaskAssignedEventCreationDTO.class, name = "TaskAssignedEvent"),
        @JsonSubTypes.Type(value = TaskCompletedEventCreationDTO.class, name = "TaskCompletedEvent"),
        @JsonSubTypes.Type(value = WorkerPositionUpdateEventCreationDTO.class, name = "WorkerPositionUpdateEvent")
})
public class EventCreationDTO extends EventDTO {

}
