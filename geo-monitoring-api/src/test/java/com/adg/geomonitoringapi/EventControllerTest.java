package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.controller.EventController;
import com.adg.geomonitoringapi.event.dto.EventCreationDTO;
import com.adg.geomonitoringapi.event.dto.LocationCreationEventCreationDTO;
import com.adg.geomonitoringapi.event.entity.LocationCreationEvent;
import com.adg.geomonitoringapi.event.factory.EventFactory;
import com.adg.geomonitoringapi.event.service.EventService;
import com.adg.geomonitoringapi.exception.UnsupportedDtoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @Mock
    private EventFactory eventFactory;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void testCreateLocationCreationEventSuccess() throws Exception {
        // Создаем данные для успешного DTO
        LocationCreationEventCreationDTO eventCreationDTO = new LocationCreationEventCreationDTO();
        eventCreationDTO.setName("Location 1");
        eventCreationDTO.setPoints(Set.of(new Point(40.7128, -74.0060)));  // Пример точки (координаты)

        // Создаем объект LocationCreationEvent, который будет возвращен фабрикой
        LocationCreationEvent createdEvent = new LocationCreationEvent();
        createdEvent.setName("Location 1");
        createdEvent.setPoints(Set.of(new Point(40.7128, -74.0060)));  // Точки совпадают с переданными
        createdEvent.setId(1L);  // Устанавливаем ID для теста

        // Настроим фабрику для реального создания события
        when(eventFactory.createEvent(eventCreationDTO)).thenReturn(createdEvent);

        // Замокать поведение сервиса для сохранения события (сервис возвращает созданное событие с установленным ID)
        when(eventService.submitEvent(any(LocationCreationEvent.class))).thenReturn(createdEvent);

        // Выполняем POST-запрос через MockMvc и проверяем статус и тело ответа
        mockMvc.perform(post("/api/events")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(eventCreationDTO)));
    }

    @Test
    public void testCreateEventUnsupportedDto() throws Exception {
        // Создаем данные для неподдерживаемого DTO
        EventCreationDTO unsupportedDto = new EventCreationDTO() {}; // Создаем анонимный класс, который не будет поддерживаться фабрикой

        // Замокать создание события фабрикой (передадим неподдерживаемый DTO)
        when(eventFactory.createEvent(unsupportedDto)).thenThrow(new UnsupportedDtoException("Unsupported DTO type"));

        // Выполняем POST-запрос через MockMvc и проверяем статус и тело ответа
        mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(unsupportedDto)))
                .andExpect(status().isBadRequest()); // Проверка HTTP статуса 400 (Bad Request)

    }
}