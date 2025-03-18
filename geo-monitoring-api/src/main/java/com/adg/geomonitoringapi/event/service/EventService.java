package com.adg.geomonitoringapi.event.service;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final Queue<Event> newEventsQueue;

    public Event submitEvent(Event event) {
        var e = eventRepository.save(event);
        newEventsQueue.add(e);
        return e;
    }
}
