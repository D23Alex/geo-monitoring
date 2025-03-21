package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LocationCreationEvent extends Event {
    private String name;

    @ElementCollection
    @OrderColumn(name = "list_index")
    private List<Point> points;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> oldState.getLocations().containsKey(getId()),
                "Невозможно создать локацию: локация с id " + getId() + " уже существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        Long newLocationId = getId();

        LocationState newLocation = LocationState.builder()
                .points(points)
                .name(name)
                .id(newLocationId)
                .build();

        var newLocations = new HashMap<>(oldState.getLocations());
        newLocations.put(newLocationId, newLocation);

        return oldState.withLocations(newLocations);
    }
}
