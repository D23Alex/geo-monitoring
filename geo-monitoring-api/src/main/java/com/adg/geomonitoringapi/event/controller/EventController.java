package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.dto.EventCreationDTO;
import com.adg.geomonitoringapi.event.dto.EventResponseDTO;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.factory.EventFactory;
import com.adg.geomonitoringapi.event.mapper.EventMapper;
import com.adg.geomonitoringapi.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventFactory eventFactory;

    @PostMapping("/{eventType}")
    public ResponseEntity<? extends EventResponseDTO> createEvent(
            @PathVariable String eventType, @RequestBody EventCreationDTO eventCreationDTO) {

        Event event = eventFactory.createEvent(eventType, eventCreationDTO);

        Event createdEvent = eventService.submitEvent(event);

        EventResponseDTO eventResponseDTO = eventMapper.mapEventToResponseDTO(createdEvent);

        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
    }
}
