package com.adg.geomonitoringapi.state.service;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.state.SystemState;

import java.time.Instant;
import java.util.stream.Stream;

public interface StateService {
    SystemState getLatestState();

    SystemState beforeEvent(Long eventId);

    SystemState atInstant(Instant t);

    default SystemState applyEvents(Stream<Event> eventsOrderedByTimestamp) {
        return applyEvents(SystemState.initial(), eventsOrderedByTimestamp);
    }

    default SystemState applyEvents(SystemState initialState, Stream<Event> eventsOrderedByTimestamp) {
        return eventsOrderedByTimestamp
                .filter(event -> event.getTimestamp().isAfter(initialState.getLastEvent().getTimestamp()))
                .reduce(SystemState.initial(),
                        (state, event) -> event.updateState(state),
                        (s1, s2) -> s2);
    }
}
