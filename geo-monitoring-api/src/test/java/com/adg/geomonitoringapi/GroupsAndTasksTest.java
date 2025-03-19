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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Worker foreman = Worker.builder().name("foremanName").build();

        Set<Worker> workers = Set.of(
                Worker.builder().name("worker1").build(),
                Worker.builder().name("worker2").build(),
                Worker.builder().name("worker3").build()
        );

        WorkerGroupCreationEvent e = WorkerGroupCreationEvent.builder()
                .groupActiveTo(initial.minusSeconds(1000000))
                .groupActiveFrom(initial.plusSeconds(1000000))
                .timestamp(initial.plusSeconds(10))
                .workers(workers)
                .foreman(foreman).build();

        eventRepository.save(e);

        SystemState state = stateService.getLatestState();

        GroupState group = state.getGroups().get(1L);

        assertEquals(foreman, group.getForeman());
        group.getWorkers().forEach(worker -> System.out.println(worker.getName()));
        assertEquals(workers, group.getWorkers());
    }
}
