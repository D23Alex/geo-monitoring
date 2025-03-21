package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@SuperBuilder
public class NothingHappenedEvent extends Event {
    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Set.of();
    }

    @Override
    public SystemState apply(SystemState oldState) {
        return oldState;
    }
}
