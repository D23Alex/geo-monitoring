package com.adg.geomonitoringapi.group.entity;

import com.adg.geomonitoringapi.worker.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Group {
    private Set<Worker> workers;
    private Worker foreman;
}
