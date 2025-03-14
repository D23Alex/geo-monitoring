package com.adg.geomonitoringapi.snapshot.service;

import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.snapshot.entity.Snapshot;
import com.adg.geomonitoringapi.snapshot.repository.SnapshotRepository;
import com.adg.geomonitoringapi.event.SystemState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public SystemState getCurrentState() {
        // Получаем последний снапшот, если он существует
        Snapshot latestSnapshot = snapshotRepository.findAll().stream()
                .max(Comparator.comparing(Snapshot::getSnapshotTime))
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
                .sorted(Comparator.comparing(Event::getTimestamp))
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
