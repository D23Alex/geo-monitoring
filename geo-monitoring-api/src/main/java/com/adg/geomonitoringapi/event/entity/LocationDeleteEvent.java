package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDeleteEvent extends Event {
    private Long locationId;

    @Override
    public SystemState updateState(SystemState oldState) {
        var newLocations = new HashMap<>(oldState.getLocations());
        LocationState removedLocation = newLocations.remove(locationId);

        if (removedLocation == null)
            throw new SystemState.StateUpdateException("Невозможно удалить локацию: локация с id "
                    + locationId + " не существует");

        return oldState.withLocations(newLocations);
    }
}
