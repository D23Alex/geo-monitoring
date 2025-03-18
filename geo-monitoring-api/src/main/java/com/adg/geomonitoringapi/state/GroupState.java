package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.Worker;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public final class GroupState {
    private Set<Worker> workers;
    private Worker foreman;
    private Instant createdAt;
    private Instant activeFrom;
    private Instant activeTo;
}
