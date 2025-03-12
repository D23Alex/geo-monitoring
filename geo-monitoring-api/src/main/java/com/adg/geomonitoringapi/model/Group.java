package com.adg.geomonitoringapi.model;

import java.util.List;
import java.util.Set;

public record Group(Set<Worker> workers, Worker brigadier) {
}
