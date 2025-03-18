package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.controller.EventController;
import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.TaskAssignedEvent;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.service.EventService;
import com.adg.geomonitoringapi.exception.EntityNotFoundException;
import com.adg.geomonitoringapi.handler.AppHandlerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new AppHandlerException()) // Добавляем обработчик ошибок
                .build();
    }

    @Test
    void createEvent_Success() throws Exception {
        TaskAssignedEventCreationDTO eventCreationDTO = new TaskAssignedEventCreationDTO();
        eventCreationDTO.setDescription("Test Task");

        TaskAssignedEvent taskEvent = new TaskAssignedEvent();
//        taskEvent.setId(1L); // Убедимся, что ID установлен
        taskEvent.setDescription("Test Task");

        when(eventService.submitEvent(any(TaskAssignedEvent.class))).thenReturn(taskEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreationDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/events/1"))) // Теперь ID корректный
                .andExpect(jsonPath("$.id").value(1));

        verify(eventService, times(1)).submitEvent(any(TaskAssignedEvent.class));
    }



    @Test
    void createEvent_UnknownType_ShouldReturnNotFound() throws Exception {
        TaskAssignedEventCreationDTO unknownDto = new TaskAssignedEventCreationDTO();
        unknownDto.setDescription("Unknown Event");

        doThrow(new EntityNotFoundException("Event type not found"))
                .when(eventService)
                .submitEvent(any(Event.class));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unknownDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event type not found"));
    }
}
