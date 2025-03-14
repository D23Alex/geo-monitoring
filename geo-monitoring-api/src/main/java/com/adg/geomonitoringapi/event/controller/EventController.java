package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository eventRepository;

    @PostMapping
    public String submitEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return "Event saved";
    }
}
