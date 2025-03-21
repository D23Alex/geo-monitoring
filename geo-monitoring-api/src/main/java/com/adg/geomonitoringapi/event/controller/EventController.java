package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.event.factory.EventFactory;
import com.adg.geomonitoringapi.event.mapper.EventMapper;
import com.adg.geomonitoringapi.event.service.EventService;
import com.adg.geomonitoringapi.exception.UnsupportedDtoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final EventFactory eventFactory;
    private final EventMapper eventMapper;

    @PostMapping("/{eventType}")
    public ResponseEntity<? extends EventResponseDTO> createEvent(
            @PathVariable String eventType, @RequestBody EventCreationDTO eventCreationDTO) {

        Class<? extends Event> eventClass = eventMapper.getEventClassFromString(eventType);

        Event event = eventFactory.createEvent(eventCreationDTO);
        if (!eventClass.isInstance(event)) {
            throw new UnsupportedDtoException("Event type mismatch");
        }

        Event createdEvent = eventService.submitEvent(event);

        EventResponseDTO eventResponseDTO = eventMapper.mapEventToResponseDTO(createdEvent);

        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
    }
}
