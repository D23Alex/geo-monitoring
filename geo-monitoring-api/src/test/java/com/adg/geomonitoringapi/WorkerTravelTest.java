package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.WorkerCreationEvent;
import com.adg.geomonitoringapi.event.entity.WorkerPositionUpdateEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.geometry.Geometry;
import com.adg.geomonitoringapi.state.service.StateService;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WorkerTravelTest {
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
        Worker worker = Worker.builder().name("Bob").build();
        Instant initial = Instant.now();

        List<Event> events = List.of(
                WorkerCreationEvent.builder()
                        .worker(worker)
                        .timestamp(initial.plusSeconds(1)).build(),

                WorkerPositionUpdateEvent.builder()
                        .latitude(60.1)
                        .longitude(50.1)
                        .workerId(1L)
                        .timestamp(initial.plusSeconds(10)).build(),

                WorkerPositionUpdateEvent.builder()
                        .latitude(60.2)
                        .longitude(50.2)
                        .workerId(1L)
                        .timestamp(initial.plusSeconds(20)).build(),

                WorkerPositionUpdateEvent.builder()
                        .latitude(60.3)
                        .longitude(50.3)
                        .workerId(1L)
                        .timestamp(initial.plusSeconds(30)).build(),

                WorkerPositionUpdateEvent.builder()
                        .latitude(60.4)
                        .longitude(50.4)
                        .workerId(1L)
                        .timestamp(initial.plusSeconds(40)).build()
        );

        eventRepository.saveAll(events);

        SystemState state = stateService.getCurrentState();

        assertEquals(1, 1);

        assertEquals(new Point(60.4, 50.4), state.getWorkers().get(1L).lastKnownPosition());

        assertEquals(
                Geometry.totalTravelDistance(List.of(
                        new Point(60.1, 50.1),
                        new Point(60.2, 50.2),
                        new Point(60.3, 50.3),
                        new Point(60.4, 50.4)
                )),
                state.getWorkers().get(1L).distanceTravelled(), 0.01
        );
    }
}
