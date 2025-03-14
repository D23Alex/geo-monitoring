package com.adg.geomonitoringapi.snapshot.service;

import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.event.SystemState;
import org.springframework.stereotype.Service;

@Service
public class StateService {
    private EventRepository eventRepository;

    public SystemState getCurrentState() {
        return eventRepository.findAll().stream()
                .reduce(SystemState.initial(),
                        (state, event) -> event.updateState(state),
                        (s1, s2) -> s2);

    }
}
