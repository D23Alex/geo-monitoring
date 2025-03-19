package com.adg.geomonitoringapi.state.service;

import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.SystemState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StateServiceWithoutSnapshots implements StateService {
    private final EventRepository eventRepository;

    @Override
    public SystemState getLatestState() {
        return applyEvents(eventRepository.findAllByOrderByTimestampAsc().stream());
    }

    @Override
    public SystemState beforeEvent(Long eventId) {
        return applyEvents(eventRepository.findAllByOrderByTimestampAsc().stream()
                .takeWhile(event -> event.getId().equals(eventId)));
    }

    @Override
    public SystemState atInstant(Instant t) {
        return applyEvents(eventRepository.findAllByTimestampBeforeOrderByTimestampAsc(t).stream());
    }

}
