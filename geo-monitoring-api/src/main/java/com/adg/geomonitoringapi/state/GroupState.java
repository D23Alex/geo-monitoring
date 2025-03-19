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
    private Set<Long> workerIds;
    private Long foremanId;
    private Instant createdAt;
    private Instant activeFrom;
    private Instant activeTo;
}
