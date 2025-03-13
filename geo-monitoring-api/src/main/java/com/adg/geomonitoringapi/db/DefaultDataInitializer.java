package com.adg.geomonitoringapi.db;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.WorkerGroupCreationEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.event.repository.WorkerRepository;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
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
        Worker brigadier = Worker.builder().name("worker1name").build();
        Worker worker2 = Worker.builder().name("worker2name").build();
        Worker worker3 = Worker.builder().name("worker3name").build();

        workerRepository.saveAll(List.of(brigadier, worker2, worker3));

        WorkerGroupCreationEvent event = WorkerGroupCreationEvent.builder()
                .brigadier(brigadier)
                .workers(Set.of(worker2, worker3))
                .groupActiveFrom(Instant.now().minusMillis(10000))
                .groupActiveTo(Instant.now().plusMillis(10000))
                .timestamp(Instant.now())
                .build();

        eventRepository.save(event);

        var allEvents = eventRepository.findAllByOrderByTimestampAsc();
        allEvents.forEach(e -> e.updateState(SystemState.initial()));
    }
}
