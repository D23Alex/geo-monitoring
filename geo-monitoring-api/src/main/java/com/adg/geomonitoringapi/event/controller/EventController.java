package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.dto.EventCreationDTO;
import com.adg.geomonitoringapi.event.dto.EventResponseDTO;
import com.adg.geomonitoringapi.event.entity.Event;
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
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventCreationDTO eventCreationDTO) {
        try {
            Event event = eventFactory.createEvent(eventCreationDTO);
            Event createdEvent = eventService.submitEvent(event);
            EventResponseDTO eventResponseDTO = mapper.map(createdEvent, EventResponseDTO.class);
            return ResponseEntity.created(URI.create("/api/events/" + createdEvent.getId())).body(eventResponseDTO);
        } catch (Exception e) {
            // Логирование ошибки
            System.out.println("Ошибка!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
