package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.NothingHappenedEvent;
import com.adg.geomonitoringapi.util.Interval;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public final class SystemState {

    public static class StateUpdateException extends RuntimeException {
        public StateUpdateException(String s) {
        }
    }

    private Map<Long, GroupState> groups;
    private Map<Long, LocationState> locations;
    private Map<Long, TaskState> tasks;
    private Map<Long, WorkerState> workers;
    private Map<Long, WorkAbsenceState> absences;
    private Event lastEvent;
    private Long eventsApplied = 0L;

    public Set<Long> activeTaskIds(Instant t) {
        return tasks.entrySet().stream()
                .filter(idAndTask -> idAndTask.getValue().getActiveInterval().contains(t))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые должны начать и закончить выполняться за определенный период
     * @param interval заданный интервал
     * @return id задач, интервалы выполнения которых полностью содержатся в заданном интервале
     */
    public Set<Long> activeTaskFullyInsideIntervalIds(Interval interval) {
        return tasks.entrySet().stream()
                .filter(idAndTask -> idAndTask.getValue().getActiveInterval().isContainedBy(interval))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые начинаются до заданного интервала, а заканчиваются после
     * @param interval заданный интервал
     * @return id задач, интервалы выполнения которых полностью содержат заданный интервал
     */
    public Set<Long> activeTaskFullyContainingIntervalIds(Interval interval) {
        return tasks.entrySet().stream()
                .filter(idAndTask -> idAndTask.getValue().getActiveInterval().contains(interval))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые будут активны хотя бы в какой-то момент из заданного интервала.
     * @param interval заданный интервал
     * @return id задач, интервалы выполнения которых пересекаются с заданным интервалом
     */
    public Set<Long> activeTaskOverlappingIntervalIds(Interval interval) {
        return tasks.entrySet().stream()
                .filter(idAndTask -> idAndTask.getValue().getActiveInterval().overlaps(interval))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Set<TaskState> activeTasks(Instant t) {
        return activeTaskIds(t).stream()
                .map(id -> tasks.get(id))
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые должны начать и закончить выполняться за определенный период
     * @param interval заданный интервал
     * @return задачи, интервалы выполнения которых полностью содержатся в заданном интервале
     */
    public Set<TaskState> activeTasksFullyInsideInterval(Interval interval) {
        return activeTaskFullyContainingIntervalIds(interval).stream()
                .map(id -> tasks.get(id))
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые начинаются до заданного интервала, а заканчиваются после
     * @param interval заданный интервал
     * @return задачи, интервалы выполнения которых полностью содержат заданный интервал
     */
    public Set<TaskState> activeTasksFullyContainingInterval(Interval interval) {
        return activeTaskFullyContainingIntervalIds(interval).stream()
                .map(id -> tasks.get(id))
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые будут активны хотя бы в какой-то момент из заданного интервала.
     * @param interval заданный интервал
     * @return задачи, интервалы выполнения которых пересекаются с заданным интервалом
     */
    public Set<TaskState> activeTasksOverlappingInterval(Interval interval) {
        return activeTaskOverlappingIntervalIds(interval).stream()
                .map(id -> tasks.get(id))
                .collect(Collectors.toSet());
    }

    public Set<TaskState> tasksForWorker(Long workerId) {
        return tasks.values().stream()
                .filter(task -> task.getAssignedWorkers().contains(workerId))
                .collect(Collectors.toSet());
    }

    /**
     * @return спикок работников, местоположение которых долго не меняется
     */
    public Set<WorkerState> idleWorkers() {
        return workers.values().stream()
                .filter(WorkerState::isIdle)
                .collect(Collectors.toSet());
    }

    /**
     * @param t момент во времени
     * @return список id работников, которые имеют задачу на данный момент времени
     */
    public Set<Long> busyWorkerIds(Instant t) {
        return activeTasks(t).stream()
                .map(TaskState::getAssignedWorkers)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * @param t момент во времени
     * @return список работников, которые имеют задачу на данный момент времени
     */
    public Set<WorkerState> busyWorkers(Instant t) {
        return busyWorkerIds(t).stream().map(id -> workers.get(id)).collect(Collectors.toSet());
    }

    public boolean isBusy(Long workerId, Instant t) {
        return busyWorkerIds(t).contains(workerId);
    }

    public Set<TaskState> tasksForWorkerAtTimestamp(Long workerId, Instant t) {
        return activeTasks(t).stream()
                .filter(task -> task.getAssignedWorkers().contains(workerId))
                .collect(Collectors.toSet());
    }

    public Set<Long> locationsForWorkerAtTimestampIds(Long workerId, Instant t) {
        return tasksForWorkerAtTimestamp(workerId, t).stream()
                .map(TaskState::getLocationId)
                .collect(Collectors.toSet());
    }

    public Set<Long> workerWithoutActiveTaskIds(Instant t) {
        return workers.keySet().stream()
                .filter(id -> !busyWorkerIds(t).contains(id))
                .collect(Collectors.toSet());
    }

    public Set<WorkerState> workersWithoutActiveTask(Instant t) {
        return workerWithoutActiveTaskIds(t).stream().map(id -> workers.get(id)).collect(Collectors.toSet());
    }

    public Set<GroupState> activeGroups(Instant t) {
        return groups.values().stream()
                .filter(group -> group.getActiveInterval().contains(t))
                .collect(Collectors.toSet());
    }

    public boolean isSubordinateAt(Long workerId, Long foremanId, Instant t) {
        return activeGroups(t).stream()
                .anyMatch(group -> Objects.equals(group.getForemanId(), foremanId)
                && group.getWorkerIds().contains(workerId));
    }

    public static SystemState initial() {
        return new SystemState(Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                NothingHappenedEvent.builder().id(0L).timestamp(Instant.EPOCH).build(),
                0L
        );
    }


}