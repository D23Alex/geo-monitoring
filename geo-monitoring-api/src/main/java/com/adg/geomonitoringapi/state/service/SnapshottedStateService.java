package com.adg.geomonitoringapi.state.service;

import com.adg.geomonitoringapi.Util;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.snapshot.entity.Snapshot;
import com.adg.geomonitoringapi.snapshot.repository.SnapshotRepository;
import com.adg.geomonitoringapi.state.SystemState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class SnapshottedStateService implements StateService {// TODO: адаптировать к конкурентным изменениям
    private final SnapshotRepository snapshotRepository;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;;
    private final Queue<Event> newEventsQueue;

    // Возможна оптимизация операций с линейным временем (фильтраций) если заменить на 2 TreeMap
    // (по временной отметке и по количеству обработанных ивентов)
    private Set<SystemState> states = new HashSet<>();

    private Long totalEvents = 0L;

    // На сколько ивентов в прошлом может быть снапшот из памяти, чтобы произошло обращение к нему, а не на диск?
    private static final Long PREFERRED_IN_MEMORY_SNAPSHOT_AGE_IN_EVENTS = 10L;
    // Через какое количество ивентов перестаём хранить снапшот в памяти?
    private static final Long IN_MEMORY_SNAPSHOT_MAX_AGE_IN_EVENTS = 1000L;
    // Какое максимальное число снапшотов может храниться в памяти?
    private static final Long IN_MEMORY_SNAPSHOT_MAX_AMOUNT = 100L;





    @Override
    public SystemState getLatestState() {
        SystemState snapshot = optimalLatestSnapshot();
        return applyEvents(snapshot,
                eventRepository.findAllByTimestampAfterOrderByTimestampAsc(snapshot.getLastEvent().getTimestamp())
                        .stream());
    }

    @Override
    public SystemState beforeEvent(Long eventId) {
        Optional<Event> e = eventRepository.findById(eventId);
        if (e.isEmpty())
            throw new EntityNotFoundException();

        return atInstant(e.get().getTimestamp());
    }

    @Override
    public SystemState atInstant(Instant t) {
        SystemState snapshot = optimalLatestSnapshotBefore(t);
        return applyEvents(snapshot,
                eventRepository.findAllByTimestampBetweenOrderByTimestampAsc(snapshot.getLastEvent().getTimestamp(),
                        t).stream());
    }

    private void updateStates() {
        List<Event> newEvents = Util.extractAll(newEventsQueue);

        Optional<Event> leastRecentUnhandledEvent = newEvents.stream().min(Comparator.comparing(Event::getTimestamp));
        if (leastRecentUnhandledEvent.isEmpty())
            return;

        totalEvents += newEvents.size();

        states = states.stream().filter(state ->
                        state.getLastEvent().getTimestamp().isBefore(leastRecentUnhandledEvent.get().getTimestamp()))
                .collect(Collectors.toSet());

        snapshotRepository.deleteAllByTimestampAfter(leastRecentUnhandledEvent.get().getTimestamp());

        optimizeInMemorySnapshots();
    }

    private void optimizeInMemorySnapshots() {
        states = states.stream() // TODO: Заинлайнить
                .filter(state -> state.getEventsApplied() < totalEvents - IN_MEMORY_SNAPSHOT_MAX_AGE_IN_EVENTS)
                .sorted(Comparator.comparingLong(SystemState::getEventsApplied).reversed())
                .limit(IN_MEMORY_SNAPSHOT_MAX_AMOUNT)
                .collect(Collectors.toSet());
    }

    private SystemState optimalLatestSnapshot() {
        return optimalLatestSnapshotBefore(Instant.MAX);
    }

    private SystemState optimalLatestSnapshotBefore(Instant t) {
        updateStates();

        Optional<SystemState> latestInMemorySnapshot = states.stream()
                .filter(state -> state.getLastEvent().getTimestamp().isBefore(t))
                .max(Comparator.comparingLong(SystemState::getEventsApplied));

        if (latestInMemorySnapshot.isPresent() && latestInMemorySnapshot.get().getEventsApplied()
                > totalEvents - PREFERRED_IN_MEMORY_SNAPSHOT_AGE_IN_EVENTS)
            return latestInMemorySnapshot.get();

        Optional<Snapshot> latestSnapshotFromDisk = snapshotRepository.findTopByTimestampBeforeOrderByTimestamp(t);
        SystemState stateFromDisk;
        if (latestSnapshotFromDisk.isPresent()) {
            try {
                stateFromDisk = objectMapper.readValue(latestSnapshotFromDisk.get().getStateJson(), SystemState.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            stateFromDisk = SystemState.initial();
        }

        if (latestInMemorySnapshot.isPresent()
                && latestInMemorySnapshot.get().getEventsApplied() > stateFromDisk.getEventsApplied())
            return latestInMemorySnapshot.get();
        return stateFromDisk;
    }

    private SystemState earliestAfter(Instant t) {
        return null;
    }
}
