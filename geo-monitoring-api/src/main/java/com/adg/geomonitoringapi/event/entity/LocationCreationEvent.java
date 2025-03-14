package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.location.entity.Point;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreationEvent {
    private String name;

    @ElementCollection
    @CollectionTable(name = "location_points", joinColumns = @JoinColumn(name = "location_id"))
    private Set<Point> points;
}
