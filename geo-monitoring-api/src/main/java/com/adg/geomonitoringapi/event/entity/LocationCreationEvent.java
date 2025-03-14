package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreationEvent extends Event {
    private String name;

    @ElementCollection
    @CollectionTable(name = "location_points", joinColumns = @JoinColumn(name = "location_id"))
    private Set<Point> points;

    @Override
    public SystemState updateState(SystemState oldState) {
        LocationState newLocation = LocationState.builder()
                .points(points)
                .name(name)
                .build();

        Long newLocationId = getId();

        if (oldState.getLocations().containsKey(newLocationId))
            throw new SystemState.StateUpdateException("Невозможно создать локацию: локация с id "
                    + newLocationId + " уже существует");

        var newLocations = new HashMap<>(oldState.getLocations());
        newLocations.put(newLocationId, newLocation);

        return oldState.withLocations(newLocations);
    }
}
