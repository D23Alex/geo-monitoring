package com.adg.geomonitoringapi.snapshot.service;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.SystemState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StateService {
    private final EventRepository eventRepository;

    public SystemState getCurrentState() {
        return applyEvents(eventRepository.findAllByOrderByTimestampAsc().stream());
    }

    public SystemState beforeEvent(Long eventId) {
        return applyEvents(eventRepository.findAllByOrderByTimestampAsc().stream()
                .takeWhile(event -> event.getId().equals(eventId)));
    }

    public SystemState atInstant(Instant t) {
        return applyEvents(eventRepository.findAllByTimestampBeforeOrderByTimestampAsc(t).stream());
    }

    private SystemState applyEvents(Stream<Event> events) {
        return events.reduce(SystemState.initial(),
                        (state, event) -> event.updateState(state),
                        (s1, s2) -> s2);
    }
}
