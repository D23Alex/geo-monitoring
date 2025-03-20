package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.entity.LocationCreationEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.service.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LocationTest {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StateService stateService;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @Test
    public void testLocationCreation() {
        Instant initial = Instant.now();

        LocationCreationEvent e = LocationCreationEvent.builder()
                .name("somelocation")
                .points(List.of(
                        new Point(60.1, 50.1),
                        new Point(70.1, 70.1),
                        new Point(23.8, 5.9)
                ))
                .timestamp(initial.plusSeconds(1))
                .build();

        eventRepository.save(e);

        SystemState state = stateService.getLatestState();

        assertEquals(1, state.getLocations().size());
    }
}
