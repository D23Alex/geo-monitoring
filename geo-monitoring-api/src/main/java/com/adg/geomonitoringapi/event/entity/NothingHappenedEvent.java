package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@SuperBuilder
public class NothingHappenedEvent extends Event {
    @Override
    public SystemState apply(SystemState oldState) {
        return oldState;
    }
}
