package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.SystemState;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.WorkerGroupCreationEvent.class, name = "WorkerGroupCreationEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskCreatedEvent.class, name = "TaskCreatedEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskAssignedEvent.class, name = "TaskAssignedEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskCompletedEvent.class, name = "TaskCompletedEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskCancelledEvent.class, name = "TaskCancelledEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.AbnormalSituationEvent.class, name = "AbnormalSituationEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.ForemanAssignmentEvent.class, name = "ForemanAssignmentEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.WorkerPositionUpdateEvent.class, name = "WorkerPositionUpdateEvent")
})
@Table
public abstract class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant timestamp;

    public abstract SystemState updateState(SystemState oldState);
}
