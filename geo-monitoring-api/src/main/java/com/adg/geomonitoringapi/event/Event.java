package com.adg.geomonitoringapi.event;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Event {
    @Id
    private Long id;
    private Instant timestamp;

    public abstract SystemState updateState(SystemState oldState);
}
