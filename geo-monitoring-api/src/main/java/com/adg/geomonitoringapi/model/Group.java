package com.adg.geomonitoringapi.model;

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
    private Worker brigadier;
}
