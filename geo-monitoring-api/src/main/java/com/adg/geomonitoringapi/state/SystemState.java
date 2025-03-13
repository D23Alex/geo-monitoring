package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.model.Group;
import com.adg.geomonitoringapi.model.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class SystemState {
    private Set<Group> futureGroups;
    private Set<Group> activeGroups;
    private Set<Worker> idleWorkers;
    private Instant timestamp;

    public static SystemState initial() {
        return new SystemState(Set.of(), Set.of(), Set.of(), Instant.now());
    }
}