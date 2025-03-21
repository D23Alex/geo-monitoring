package com.adg.geomonitoringapi.state.service;

import com.adg.geomonitoringapi.util.Util;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Делает снапшоты как в памяти (полезно для небольшого количества свежих снапшотов, чтоб не лезть на диск),
 * так и на диске (всё остальное).
 * Invariants:
 * 1) Гарантируется, что при вычислении состояния на основе снпашота и списка ивентов снапшот включает в себя ВСЕ
 * ивенты, которые попали в нашу систему до любого из добавляемых к снапшоту ивентов.
 * То есть НЕВОЗМОЖНА следующая ситуация: есть снапшот на момент времени t(x), после его создания в систему пришли
 * ивенты с отметками t(a) и t(b), причем t(a) < t(x) < t(b). Вычисляется состояние: берем снапшот и примением к нему
 * событие с временем t(b). При этом событие t(a) остаётся необработанным и получается не то же состояние,
 * что должно было получиться, если бы мы применяли все состояния поочередно с нулевого, как и нужно.
 * 2) Гарантируется, что в любой момент времени каждый находящийся в памяти снапшот обязательно содержит
 * результат всех ивентов из снапшотов, у которых timestamp (и, соответственно, eventsApplied) меньше данного.
 * Не гарантируется:
 * 1) Не гарантируется, что снапшот содержит результат всех существующих в системе ивентов.
 * 2) Синхронизации между снапшотами в памяти и на диске не предполагается, на неё надеяться нельзя.
 */
@Service
@Primary
@RequiredArgsConstructor
public class SnapshottedStateService implements StateService {
    private final SnapshotRepository snapshotRepository;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;
    private final Queue<Event> newEventsQueue;

    // Возможна оптимизация операций с линейным временем (фильтраций), если заменить на 2 TreeMap
    // (по временной отметке и по количеству обработанных ивентов)
    private Set<SystemState> states = new HashSet<>();

    /**
     * Поскольку количество ивентов может только расти, и чем быстрее растет количество ивентов в системе,
     * тем быстрее устаревают снапшоты, мы не боимся изменений этой величины другим тредом внутри наших вычислений,
     * поскольку увеличение её может лишь увеличить требования к актуальности снапшотов,
     * но никак не ослабить требования, а значит ни один устаревший снапшот не будет считаться актуальным.
     */
    private final AtomicLong totalEvents = new AtomicLong(0);
    private Long eventsInLatestOnDiskSnapshot = 0L;

    // На сколько ивентов в прошлом может быть снапшот из памяти, чтобы произошло обращение к нему, а не на диск?
    private static final Long PREFERRED_IN_MEMORY_SNAPSHOT_AGE_IN_EVENTS = 10L;
    // Через какое количество ивентов перестаём хранить снапшот в памяти?
    private static final Long IN_MEMORY_SNAPSHOT_MAX_AGE_IN_EVENTS = 1000L;
    // Какое максимальное число снапшотов может храниться в памяти?
    private static final Long IN_MEMORY_SNAPSHOT_MAX_AMOUNT = 100L;
    // при достижении какого возраста самого свежего (больше всего ивентов) снапшота на диске нужно создать новый?
    private static final Long MAX_LATEST_ON_DISK_SNAPSHOT_AGE = 50L;

    @Override
    public SystemState getLatestState() {
        return atInstant(Util.SOME_TIMESTAMP_IN_FUTURE);
    }

    @Override
    public SystemState beforeEvent(Long eventId) {
        Optional<Event> e = eventRepository.findById(eventId);
        if (e.isEmpty())
            throw new EntityNotFoundException();

        return atInstant(e.get().getTimestamp());
    }

    /**
     * Требуется, чтобы на момент получения списка будущих ивентов для применения к снапшоту
     * снапшот этот содержал в себе все содержащиеся в бд ивенты с timestamp'ами меньше, чем момент снапшотирования.
     * То есть если пришёл ивент из прошлого (прошлого для снапшота), снапшот должен быть гарантированно инвалидирован.
     * Этот метод даёт даже более строгую гарантию, чем требуется, так как инвалидировать снапшот могут даже ивенты,
     * пришедшие уже после выборки ивентов для применения к снапшоту.
     * Но так проще, чем синхронизировать с хранилищем и локать.
     * Допускаю, что оверхед от retry этого метода при инвлидации снапшота превысит оверхед от ожидания на локах
     * при синхронизации, но и так нормально.
     * Возможна ли в реальности ситуация, когда это решение зациклится?
     * @param t момент во времени
     * @return состояние системы на заданный момент
     */
    @Override
    public SystemState atInstant(Instant t) {
        SystemState snapshot = optimalLatestSnapshotBefore(t);
        var events = eventRepository
                .findAllByTimestampBetweenOrderByTimestampAsc(snapshot.getLastProcessedEvent().getTimestamp(), t);

        List<Event> newEvents = Util.atomicallyExtractAll(newEventsQueue);
        Optional<Event> leastRecentUnhandledEvent = newEvents.stream().min(Comparator.comparing(Event::getTimestamp));

        if (leastRecentUnhandledEvent.isEmpty())
            return applyEvents(snapshot, events.stream());

        totalEvents.addAndGet(newEvents.size());
        atomicallyInvalidateOutdatedSnapshotsInMemory(leastRecentUnhandledEvent.get().getTimestamp());
        snapshotRepository.deleteAllByTimestampAfter(leastRecentUnhandledEvent.get().getTimestamp());

        boolean snapshotIsOutdated =
                leastRecentUnhandledEvent.get().getTimestamp().isBefore(snapshot.getLastProcessedEvent().getTimestamp());
        if (snapshotIsOutdated)
            return atInstant(t);

        SystemState returnedState = applyEvents(snapshot, events.stream());
        trySaveSnapshot(returnedState);

        return returnedState;
    }

    private synchronized void atomicallyInvalidateOutdatedSnapshotsInMemory(Instant t) {
        states = states.stream()
                .filter(state ->
                        // убрать невалидные более снапшоты
                        state.getLastProcessedEvent().getTimestamp().isBefore(t)
                                // убрать слишком старые снапшоты
                                && state.getEventsProcessed() >= totalEvents.get() - IN_MEMORY_SNAPSHOT_MAX_AGE_IN_EVENTS)
                .sorted(Comparator.comparingLong(SystemState::getEventsProcessed).reversed())
                .limit(IN_MEMORY_SNAPSHOT_MAX_AMOUNT)
                .collect(Collectors.toSet());
    }

    private SystemState optimalLatestSnapshotBefore(Instant t) {
        Optional<SystemState> latestInMemorySnapshot = states.stream()
                .filter(state -> state.getLastProcessedEvent().getTimestamp().isBefore(t))
                .max(Comparator.comparingLong(SystemState::getEventsProcessed));

        if (latestInMemorySnapshot.isPresent() && latestInMemorySnapshot.get().getEventsProcessed()
                > totalEvents.get() - PREFERRED_IN_MEMORY_SNAPSHOT_AGE_IN_EVENTS)
            return latestInMemorySnapshot.get();

        Optional<Snapshot> latestSnapshotFromDisk = snapshotRepository.findTopByTimestampBeforeOrderByTimestamp(t);
        SystemState stateFromDisk = latestSnapshotFromDisk.isPresent() ?
                parseFromJson(latestSnapshotFromDisk.get().getStateJson()) : SystemState.initial();

        boolean isInMemoryStateNewer = latestInMemorySnapshot.isPresent()
                && latestInMemorySnapshot.get().getEventsProcessed() > stateFromDisk.getEventsProcessed();

        return isInMemoryStateNewer ? latestInMemorySnapshot.get() : stateFromDisk;
    }

    private void trySaveSnapshot(SystemState state) {
        saveStateToDiskIfLatestIsTooOld(state);
        states.add(state);
    }

    /**
     * "synchronized" не будет bottleneck потому что в большинстве случаев он завершается после первого if
     * (а если вдруг это не так, то увеличь MAX_LATEST_ON_DISK_SNAPSHOT_AGE)
     * @param state состояние-кандидат на сохранение
     */
    private synchronized void saveStateToDiskIfLatestIsTooOld(SystemState state) {
        if (state.getEventsProcessed() < eventsInLatestOnDiskSnapshot + MAX_LATEST_ON_DISK_SNAPSHOT_AGE)
            return;

        try {
            snapshotRepository.save(Snapshot.builder()
                    .timestamp(state.getLastProcessedEvent().getTimestamp())
                    .stateJson(objectMapper.writeValueAsString(state)).build());
            eventsInLatestOnDiskSnapshot = state.getEventsProcessed();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private SystemState parseFromJson(String stateAsJson) {
        try {
            return objectMapper.readValue(stateAsJson, SystemState.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
