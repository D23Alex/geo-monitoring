package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.WorkerCreationEvent;
import com.adg.geomonitoringapi.event.entity.WorkerGroupCreationEvent;
import com.adg.geomonitoringapi.event.entity.WorkerPositionUpdateEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.geometry.Geometry;
import com.adg.geomonitoringapi.state.GroupState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.service.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GroupsAndTasksTest {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StateService stateService;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testGroupCreation() {
        Instant initial = Instant.now();
        List<Worker> workers = List.of(
                Worker.builder().name("worker1").build(),
                Worker.builder().name("worker2").build(),
                Worker.builder().name("worker3").build()
        );
        Worker foreman = Worker.builder().name("foremanName").build();

        List<Event> events = List.of(
                WorkerCreationEvent.builder()
                        .worker(workers.get(0))
                        .timestamp(initial.plusSeconds(1)).build(),
                WorkerCreationEvent.builder()
                        .worker(workers.get(1))
                        .timestamp(initial.plusSeconds(2)).build(),
                WorkerCreationEvent.builder()
                        .worker(workers.get(2))
                        .timestamp(initial.plusSeconds(3)).build(),
                WorkerCreationEvent.builder()
                        .worker(foreman)
                        .timestamp(initial.plusSeconds(4)).build(),

                WorkerGroupCreationEvent.builder()
                        .groupActiveFrom(initial.minusSeconds(1000000))
                        .groupActiveTo(initial.plusSeconds(1000000))
                        .timestamp(initial.plusSeconds(10))
                        .workerIds(Set.of(1L, 2L, 3L))
                        .foremanId(4L).build()
        );

        eventRepository.saveAll(events);

        SystemState state = stateService.getLatestState();
        GroupState group = state.getGroups().get(5L);

        assertEquals(foreman.getName(), state.getWorkers().get(group.getForemanId()).getName());
        assertEquals(3, group.getWorkerIds().size());
        group.getWorkerIds().forEach(workerId ->
                assertTrue(workers.stream().map(Worker::getName).collect(Collectors.toSet())
                        .contains(state.getWorkers().get(workerId).getName()))
        );
    }
}
