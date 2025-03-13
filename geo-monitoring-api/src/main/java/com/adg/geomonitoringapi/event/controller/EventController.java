package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public String submitEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return "Event saved";
    }
}
