package com.adg.geomonitoringapi.event;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event {
    @Id
    private Long id;
    private Instant timestamp;

    public abstract SystemState updateState(SystemState oldState);
}
