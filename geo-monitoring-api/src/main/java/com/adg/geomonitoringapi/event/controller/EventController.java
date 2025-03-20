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
        try {
            EventResponseDTO eventResponseDTO;

            if (eventCreationDTO instanceof LocationCreationEventCreationDTO) {
                LocationCreationEvent event = (LocationCreationEvent) eventFactory.createEvent(eventCreationDTO);
                LocationCreationEvent createdEvent = (LocationCreationEvent) eventService.submitEvent(event);
                eventResponseDTO = mapper.map(createdEvent, LocationCreationEventResponseDTO.class);
                return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
            } else if (eventCreationDTO instanceof AbnormalSituationEventCreationDTO) {
                AbnormalSituationEvent event = (AbnormalSituationEvent) eventFactory.createEvent(eventCreationDTO);
                AbnormalSituationEvent createdEvent = (AbnormalSituationEvent) eventService.submitEvent(event);
                eventResponseDTO = mapper.map(createdEvent, AbnormalSituationEventResponseDTO.class);
                return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
            } else if (eventCreationDTO instanceof TaskAssignedEventCreationDTO) {
                TaskAssignedEvent event = (TaskAssignedEvent) eventFactory.createEvent(eventCreationDTO);
                TaskAssignedEvent createdEvent = (TaskAssignedEvent) eventService.submitEvent(event);
                eventResponseDTO = mapper.map(createdEvent, TaskAssignedEventResponseDTO.class);
                return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
            } else if (eventCreationDTO instanceof TaskCancelledEventCreationDTO) {
                TaskCancelledEvent event = (TaskCancelledEvent) eventFactory.createEvent(eventCreationDTO);
                TaskCancelledEvent createdEvent = (TaskCancelledEvent) eventService.submitEvent(event);
                eventResponseDTO = mapper.map(createdEvent, TaskCancelledEventResponseDTO.class);
                return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
            } else if (eventCreationDTO instanceof TaskCompletedEventCreationDTO) {
                TaskCompletedEvent event = (TaskCompletedEvent) eventFactory.createEvent(eventCreationDTO);
                TaskCompletedEvent createdEvent = (TaskCompletedEvent) eventService.submitEvent(event);
                eventResponseDTO = mapper.map(createdEvent, TaskCompletedEventResponseDTO.class);
                return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
            } else if (eventCreationDTO instanceof WorkerGroupCreationEventCreationDTO) {
                WorkerGroupCreationEvent event = (WorkerGroupCreationEvent) eventFactory.createEvent(eventCreationDTO);
                WorkerGroupCreationEvent createdEvent = (WorkerGroupCreationEvent) eventService.submitEvent(event);
                eventResponseDTO = mapper.map(createdEvent, WorkerGroupCreationEventResponseDTO.class);
                return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
            } else {
                throw new IllegalArgumentException("Unsupported Event DTO type: " + eventCreationDTO.getClass().getName());
            }
        } catch (Exception e) {
            System.out.println("Ошибка при создании события!");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
