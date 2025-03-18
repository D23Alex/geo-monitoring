package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.event.service.EventService;
import com.adg.geomonitoringapi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final ModelMapper mapper = new ModelMapper();

    private static final Map<Class<? extends EventCreationDTO>, Class<? extends Event>> eventTypeMapping = new HashMap<>();
    private static final Map<Class<? extends Event>, Class<? extends EventResponseDTO>> eventDtoMapping = new HashMap<>();

    static {
        eventTypeMapping.put(AbnormalSituationEventCreationDTO.class, AbnormalSituationEvent.class);
        eventTypeMapping.put(LocationCreationEventCreationDTO.class, LocationCreationEvent.class);
        eventTypeMapping.put(TaskAssignedEventCreationDTO.class, TaskAssignedEvent.class);
        eventTypeMapping.put(TaskCancelledEventCreationDTO.class, TaskCancelledEvent.class);
        eventTypeMapping.put(TaskCompletedEventCreationDTO.class, TaskCompletedEvent.class);
        eventTypeMapping.put(TaskCreatedEventCreationDTO.class, WorkerGroupCreationEvent.class);
        eventTypeMapping.put(WorkerPositionUpdateEventCreationDTO.class, WorkerPositionUpdateEvent.class);

        eventDtoMapping.put(AbnormalSituationEvent.class, AbnormalSituationEventResponseDTO.class);
        eventDtoMapping.put(LocationCreationEvent.class, LocationCreationEventResponseDTO.class);
        eventDtoMapping.put(TaskAssignedEvent.class, TaskAssignedEventResponseDTO.class);
        eventDtoMapping.put(TaskCancelledEvent.class, TaskCancelledEventResponseDTO.class);
        eventDtoMapping.put(TaskCompletedEvent.class, TaskCompletedEventResponseDTO.class);
        eventDtoMapping.put(WorkerPositionUpdateEvent.class, WorkerPositionUpdateEventResponseDTO.class);
    }

    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventCreationDTO eventCreationDTO) {
        Event event = mapToEntity(eventCreationDTO);
        Event createdEvent = eventService.submitEvent(event);
        EventResponseDTO eventResponseDTO = mapToDto(createdEvent);
        return ResponseEntity.created(URI.create("/api/events/" + event.getId())).body(eventResponseDTO);
    }

    private Event mapToEntity(EventCreationDTO eventCreationDTO) {
        Class<? extends Event> eventClass = eventTypeMapping.get(eventCreationDTO.getClass());

        if (eventClass != null) {
            return mapper.map(eventCreationDTO, eventClass);
        } else {
            throw new EntityNotFoundException("Event type not found for DTO: " + eventCreationDTO.getClass().getName());
        }
    }

    private EventResponseDTO mapToDto(Event event) {
        Class<? extends EventResponseDTO> eventDtoClass = eventDtoMapping.get(event.getClass());

        if (eventDtoClass != null) {
            return mapper.map(event, eventDtoClass);
        } else {
            throw new EntityNotFoundException("Event type not found for entity: " + event.getClass().getName());
        }
    }

}
