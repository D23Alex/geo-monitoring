package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant timestamp;

    public abstract SystemState updateState(SystemState oldState);
}
