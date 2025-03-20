package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.dto.EventCreationDTO;
import com.adg.geomonitoringapi.event.dto.LocationCreationEventCreationDTO;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Testcontainers
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@Slf4j
public class EventControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
        eventRepository.deleteAll();
    }

    @Test
    public void testCreateLocationCreationEventSuccess() throws Exception {
        LocationCreationEventCreationDTO eventCreationDTO = new LocationCreationEventCreationDTO();
        eventCreationDTO.setName("Location 1");
        eventCreationDTO.setPoints(Set.of(new Point(40.7128, -74.0060),
                new Point(30.1235, 59.3432)));
        eventCreationDTO.setTimestamp(Instant.now());

        mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventCreationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Location 1"))
                .andExpect(jsonPath("$.points[0].latitude").value(30.1235))
                .andExpect(jsonPath("$.points[0].longitude").value(59.3432))
                .andExpect(jsonPath("$.points[1].latitude").value(40.7128))
                .andExpect(jsonPath("$.points[1].longitude").value(-74.006))
                .andExpect(jsonPath("$.points").isArray())
                .andExpect(jsonPath("$.timestamp").exists());

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventCreationDTO)))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        log.info("Response Content: {}", responseContent);

    }
    @Test
    public void testCreateEventUnsupportedDto() throws Exception {
        // Создание неправильного DTO
        EventCreationDTO unsupportedDto = new EventCreationDTO() {};

        mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(unsupportedDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Malformed JSON"))
                .andExpect(jsonPath("$.message").value("Invalid or malformed JSON in the request body"))
                .andExpect(jsonPath("$.timestamp").exists());

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(unsupportedDto)))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        log.info("Response Content: {}", responseContent);
    }
}
