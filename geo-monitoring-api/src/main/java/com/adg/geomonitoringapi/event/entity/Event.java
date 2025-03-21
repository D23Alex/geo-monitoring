package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Set;

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
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskAssignedEvent.class, name = "TaskAssignedEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskCompletedEvent.class, name = "TaskCompletedEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.TaskCancelledEvent.class, name = "TaskCancelledEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.AbnormalSituationEvent.class, name = "AbnormalSituationEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.WorkerPositionUpdateEvent.class, name = "WorkerPositionUpdateEvent"),
        @JsonSubTypes.Type(value = com.adg.geomonitoringapi.event.entity.LocationCreationEvent.class, name = "LocationCreationEvent")
})
@Table
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant timestamp;
  
    @PreUpdate
    private void validateImmutability() {
        throw new UnsupportedOperationException("Ивенты нельзя изменять.");
    }

    public boolean canBeApplied(SystemState state) {
        return oldStateIssues(state).isEmpty();
    }

    public abstract Set<String> oldStateIssues(SystemState oldState);

    public final SystemState updateState(SystemState oldState) {
        var newIssues = new HashMap<>(oldState.getIssuesWithEvents());
        newIssues.put(id, oldStateIssues(oldState));

        if (!canBeApplied(oldState)) {
            return oldState
                    .withLastProcessedEvent(this)
                    .withEventsProcessed(oldState.getEventsProcessed() + 1)
                    .withIssuesWithEvents(newIssues);
        }

        return apply(oldState)
                .withLastProcessedEvent(this)
                .withLastAppliedEvent(this)
                .withEventsProcessed(oldState.getEventsProcessed() + 1)
                .withEventsApplied(oldState.getEventsApplied() + 1)
                .withIssuesWithEvents(newIssues);
    }

    public abstract SystemState apply(SystemState oldState);
}
