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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateEvent extends Event {
    private Long locationId;

    @ElementCollection
    @CollectionTable(name = "location_points", joinColumns = @JoinColumn(name = "location_id"))
    @OrderColumn(name = "list_index")
    private List<Point> points;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getLocations().containsKey(locationId),
                "Невозможно обновить локацию: локация с id " + locationId + " не существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        LocationState oldLocation = oldState.getLocations().get(locationId);
        LocationState updatedLocation = oldLocation.withPoints(points);

        var newLocations = new HashMap<>(oldState.getLocations());
        newLocations.put(locationId, updatedLocation);

        return oldState.withLocations(newLocations);
    }
}
