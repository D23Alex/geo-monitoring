package com.adg.geomonitoringapi.snapshot.service;

import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.SystemState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateService {
    private final EventRepository eventRepository;

    public SystemState getCurrentState() {
        return eventRepository.findAll().stream()
                .reduce(SystemState.initial(),
                        (state, event) -> event.updateState(state),
                        (s1, s2) -> s2);

    }
}
