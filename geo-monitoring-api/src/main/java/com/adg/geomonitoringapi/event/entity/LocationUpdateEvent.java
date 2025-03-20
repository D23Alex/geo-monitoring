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
public class LocationUpdateEvent extends Event {
    private Long locationId;

    @ElementCollection
    private Set<Point> points;

    @Override
    public SystemState apply(SystemState oldState) {
        LocationState oldLocation = oldState.getLocations().get(locationId);
        LocationState updatedLocation = oldLocation.withPoints(points);

        if (updatedLocation == null)
            throw new SystemState.StateUpdateException("Невозможно обновить локацию: локация с id "
                    + locationId + " не существует");

        var newLocations = new HashMap<>(oldState.getLocations());
        newLocations.put(locationId, updatedLocation);

        return oldState.withLocations(newLocations);
    }
}
