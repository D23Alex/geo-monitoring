package com.adg.geomonitoringapi.db;

import com.adg.geomonitoringapi.event.entity.WorkerGroupCreationEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.worker.repository.WorkerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
public class DefaultDataInitializer {
    private final EventRepository eventRepository;
    private final WorkerRepository workerRepository;

    public DefaultDataInitializer(EventRepository eventRepository, WorkerRepository workerRepository) {
        this.eventRepository = eventRepository;
        this.workerRepository = workerRepository;
    }

    @PostConstruct
    public void addDefaultData() {
        Worker bigBoss = Worker.builder().name("worker1name").build();
        Worker worker2 = Worker.builder().name("worker2name").build();
        Worker worker3 = Worker.builder().name("worker3name").build();

        workerRepository.saveAll(List.of(bigBoss, worker2, worker3));

        WorkerGroupCreationEvent event = WorkerGroupCreationEvent.builder()
                .foreman(bigBoss)
                .workers(Set.of(worker2, worker3))
                .groupActiveFrom(Instant.now().minusMillis(10000))
                .groupActiveTo(Instant.now().plusMillis(10000))
                .build();
        event.setTimestamp(Instant.now());

        eventRepository.save(event);

        var allEvents = eventRepository.findAllByOrderByTimestampAsc();
        allEvents.forEach(e -> e.updateState(SystemState.initial()));
    }
}
