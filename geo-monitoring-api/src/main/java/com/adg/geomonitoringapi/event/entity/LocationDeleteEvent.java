package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDeleteEvent extends Event {
    private Long locationId;

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getLocations().containsKey(locationId),
                "Невозможно удалить локацию: локация с id " + locationId + " не существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        var newLocations = new HashMap<>(oldState.getLocations());
        newLocations.remove(locationId);

        return oldState.withLocations(newLocations);
    }
}
