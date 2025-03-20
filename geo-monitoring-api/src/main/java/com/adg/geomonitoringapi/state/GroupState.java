package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.util.Interval;
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
    private Long id;
    private Set<Long> workerIds;
    private Long foremanId;
    private Instant createdAt;
    private Interval activeInterval;
}
