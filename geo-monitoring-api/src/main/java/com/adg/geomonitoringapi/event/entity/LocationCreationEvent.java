package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreationEvent extends Event{
    private String name;

    @ElementCollection
    @CollectionTable(name = "location_points", joinColumns = @JoinColumn(name = "location_id"))
    private Set<Point> points;

    @Override
    public SystemState updateState(SystemState oldState) {
        return null;
    }
}
