package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.event.factory.EventFactory;
import com.adg.geomonitoringapi.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final EventFactory eventFactory;
    private final ModelMapper mapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreationDTO eventCreationDTO) {
        Event event = eventFactory.createEvent(eventCreationDTO);
        Event createdEvent = eventService.submitEvent(event);
        EventResponseDTO eventResponseDTO = mapEventToResponseDTO(createdEvent);
        return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
    }

    private EventResponseDTO mapEventToResponseDTO(Event event) {
        if (event instanceof LocationCreationEvent) {
            return mapper.map(event, LocationCreationEventResponseDTO.class);
        } else if (event instanceof AbnormalSituationEvent) {
            return mapper.map(event, AbnormalSituationEventResponseDTO.class);
        } else if (event instanceof TaskAssignedEvent) {
            return mapper.map(event, TaskAssignedEventResponseDTO.class);
        } else if (event instanceof TaskCancelledEvent) {
            return mapper.map(event, TaskCancelledEventResponseDTO.class);
        } else if (event instanceof TaskCompletedEvent) {
            return mapper.map(event, TaskCompletedEventResponseDTO.class);
        } else if (event instanceof WorkerGroupCreationEvent) {
            return mapper.map(event, WorkerGroupCreationEventResponseDTO.class);
        } else {
            throw new IllegalArgumentException("Unsupported Event type: " + event.getClass().getName());
        }
    }
}
