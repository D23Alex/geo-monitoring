package com.adg.geomonitoringapi.snapshot.service;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.snapshot.entity.Snapshot;
import com.adg.geomonitoringapi.snapshot.repository.SnapshotRepository;
import com.adg.geomonitoringapi.state.SystemState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public SnapshotService(SnapshotRepository snapshotRepository, EventRepository eventRepository, ObjectMapper objectMapper) {
        this.snapshotRepository = snapshotRepository;
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    public SystemState getCurrentState() {
        // Получаем последний снапшот, если он существует
        Snapshot latestSnapshot = snapshotRepository.findAll().stream()
                .max((s1, s2) -> s1.getSnapshotTime().compareTo(s2.getSnapshotTime()))
                .orElse(null);
        SystemState state;
        Instant snapshotTime = Instant.EPOCH;
        if (latestSnapshot != null) {
            try {
                state = objectMapper.readValue(latestSnapshot.getStateJson(), SystemState.class);
                snapshotTime = latestSnapshot.getSnapshotTime();
            } catch (Exception e) {
                state = SystemState.initial();
            }
        } else {
            state = SystemState.initial();
        }

        // Оборачиваем snapshotTime в final переменную для использования в лямбда-выражении
        final Instant effectiveSnapshotTime = snapshotTime;

        // Применяем события, произошедшие после снапшота
        List<Event> newEvents = eventRepository.findAll().stream()
                .filter(e -> e.getTimestamp().isAfter(effectiveSnapshotTime))
                .sorted((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()))
                .toList();

        for (Event event : newEvents) {
            state = event.updateState(state);
        }
        return state;
    }

    // Создание нового снапшота текущего состояния
    public void createSnapshot() {
        SystemState state = getCurrentState();
        Snapshot snapshot = new Snapshot();
        snapshot.setSnapshotTime(Instant.now());
        try {
            snapshot.setStateJson(objectMapper.writeValueAsString(state));
        } catch (Exception e) {
            //TODO Надо подрубить logger
            e.printStackTrace();
        }
        snapshotRepository.save(snapshot);
    }
}
