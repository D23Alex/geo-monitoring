package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TaskCreatedEvent extends Event { // TODO: удалить, дублирует TaskAssignedEvent
    private Long taskId;
    private String description;
    private Double latitude; // опционально
    private Double longitude;
    private String completionCriteria;
    private Instant createdAt;

    @Override
    public SystemState updateState(SystemState oldState) {
        return null;
    }
}
