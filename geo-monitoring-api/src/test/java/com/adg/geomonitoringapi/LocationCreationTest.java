package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.entity.LocationCreationEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.service.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LocationCreationTest {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StateService stateService;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @Test
    public void testWorkerPositionUpdates() {
        eventRepository.save(LocationCreationEvent.builder()
                .name("dadaa")
                .timestamp(Instant.now()).build());
        assertEquals(1, eventRepository.findAll().size());
    }
}
