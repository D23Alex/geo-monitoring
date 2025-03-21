package com.adg.geomonitoringapi.state.service;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.state.SystemState;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Stream;

@Service
public interface StateService {
    SystemState getLatestState();

    SystemState beforeEvent(Long eventId);

    SystemState atInstant(Instant t);

    default SystemState applyEvents(Stream<Event> eventsOrderedByTimestamp) {
        return applyEvents(SystemState.initial(), eventsOrderedByTimestamp);
    }

    default SystemState applyEvents(SystemState initialState, Stream<Event> eventsOrderedByTimestamp) {
        return eventsOrderedByTimestamp
                .filter(event -> event.getTimestamp().isAfter(initialState.getLastProcessedEvent().getTimestamp()))
                .reduce(SystemState.initial(),
                        (state, event) -> event.updateState(state),
                        (s1, s2) -> s2);
    }
}
