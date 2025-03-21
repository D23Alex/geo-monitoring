package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.entity.Event;
import com.adg.geomonitoringapi.event.entity.NothingHappenedEvent;
import com.adg.geomonitoringapi.geometry.Geometry;
import com.adg.geomonitoringapi.util.Interval;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        return activeTasks(t).stream().map(TaskState::getId).collect(Collectors.toSet());
    }
    /**
     * Узнать задачи, которые должны начать и закончить выполняться за определенный период
     * @param interval заданный интервал
     * @return id задач, интервалы выполнения которых полностью содержатся в заданном интервале
     */
    public Set<Long> activeTaskFullyInsideIntervalIds(Interval interval) {
        return activeTasksFullyInsideInterval(interval).stream().map(TaskState::getId).collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые начинаются до заданного интервала, а заканчиваются после
     * @param interval заданный интервал
     * @return id задач, интервалы выполнения которых полностью содержат заданный интервал
     */
    public Set<Long> activeTaskFullyContainingIntervalIds(Interval interval) {
        return activeTasksFullyContainingInterval(interval).stream().map(TaskState::getId).collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые будут активны хотя бы в какой-то момент из заданного интервала.
     * @param interval заданный интервал
     * @return id задач, интервалы выполнения которых пересекаются с заданным интервалом
     */
    public Set<Long> activeTaskOverlappingIntervalIds(Interval interval) {
        return activeTasksOverlappingInterval(interval).stream().map(TaskState::getId).collect(Collectors.toSet());
    }

    public Set<TaskState> activeTasks(Instant t) {
        return tasks.values().stream()
                .filter(task -> task.isActive(t))
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые должны начать и закончить выполняться за определенный период
     * @param interval заданный интервал
     * @return задачи, интервалы выполнения которых полностью содержатся в заданном интервале
     */
    public Set<TaskState> activeTasksFullyInsideInterval(Interval interval) {
        return tasks.values().stream()
                .filter(task -> task.getActiveInterval().isContainedBy(interval))
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые начинаются до заданного интервала, а заканчиваются после
     * @param interval заданный интервал
     * @return задачи, интервалы выполнения которых полностью содержат заданный интервал
     */
    public Set<TaskState> activeTasksFullyContainingInterval(Interval interval) {
        return tasks.values().stream()
                .filter(task -> task.getActiveInterval().contains(interval))
                .collect(Collectors.toSet());
    }

    /**
     * Узнать задачи, которые будут активны хотя бы в какой-то момент из заданного интервала.
     * @param interval заданный интервал
     * @return задачи, интервалы выполнения которых пересекаются с заданным интервалом
     */
    public Set<TaskState> activeTasksOverlappingInterval(Interval interval) {
        return tasks.values().stream()
                .filter(task -> task.getActiveInterval().overlaps(interval))
                .collect(Collectors.toSet());
    }

    public Set<TaskState> tasksForWorker(Long workerId) {
        return tasks.values().stream()
                .filter(task -> task.getAssignedWorkers().contains(workerId))
                .collect(Collectors.toSet());
    }

    public Set<GroupState> groupsForWorker(Long workerId) {
        return groups.values().stream()
                .filter(group -> group.getWorkerIds().contains(workerId))
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
     * @return список работников, которые имеют задачу на данный момент времени
     */
    public Set<WorkerState> busyWorkers(Instant t) {
        return activeTasks(t).stream()
                .map(TaskState::getAssignedWorkers)
                .flatMap(Set::stream)
                .map(workerId -> workers.get(workerId))
                .collect(Collectors.toSet());
    }

    public Set<WorkerState> workersWithoutActiveTask(Instant t) {
        return workers.values().stream()
                .filter(worker -> !busyWorkers(t).contains(worker))
                .collect(Collectors.toSet());
    }

    public boolean isBusy(Long workerId, Instant t) {
        return busyWorkers(t).stream().anyMatch(worker -> Objects.equals(worker.getId(), workerId));
    }

    public Set<TaskState> activeTasksForWorkerAtTimestamp(Long workerId, Instant t) {
        return activeTasks(t).stream()
                .filter(task -> task.getAssignedWorkers().contains(workerId))
                .collect(Collectors.toSet());
    }

    /**
     * @param workerId id
     * @param t момент времени
     * @return локации, в которых у рабочего есть задания на некоторый момент времени
     */
    public Set<LocationState> expectedWorkerLocationsAtTimestamp(Long workerId, Instant t) {
        return activeTasksForWorkerAtTimestamp(workerId, t).stream()
                .map(task -> locations.get(task.getLocationId()))
                .collect(Collectors.toSet());
    }

    public Set<GroupState> activeGroups(Instant t) {
        return groups.values().stream()
                .filter(group -> group.getActiveInterval().contains(t))
                .collect(Collectors.toSet());
    }

    public Set<GroupState> activeGroupsForWorker(Long workerId, Instant t) {
        return activeGroups(t).stream()
                .filter(group -> group.getWorkerIds().contains(workerId))
                .collect(Collectors.toSet());
    }

    public boolean isSubordinateAt(Long workerId, Long foremanId, Instant t) {
        return activeGroups(t).stream()
                .anyMatch(group -> Objects.equals(group.getForemanId(), foremanId)
                && group.getWorkerIds().contains(workerId));
    }

    public Set<WorkerState> subordinatesAt(Long foremanId, Instant t) {
        return workers.values().stream()
                .filter(w -> isSubordinateAt(w.getId(), foremanId, t))
                .collect(Collectors.toSet());
    }

    public Map<Long, Optional<Point>> lastKnownWorkerPositions() {
        return workers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().lastKnownPosition()
                ));
    }

    public Map<Long, Optional<Point>> lastKnownWorkerPositionsBy(Instant t) {
        return workers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().lastKnownPositionBy(t)
                ));
    }

    public Map<Long, Optional<Point>> approximateWorkerPositions(Instant t) {
        return workers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().approximatePosition(t)
                ));
    }

    public Set<LocationState> locationsContainingPoint(Point p) {
        return locations.values().stream()
                .filter(l -> Geometry.isPointInPolygon(p, l.getPoints()))
                .collect(Collectors.toSet());
    }

    public Map<Long, Set<LocationState>> lastKnownLocationsForEachWorker() {
        return lastKnownWorkerPositions().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> locationsContainingPoint(entry.getValue().orElse(Point.SOME_FAR_AWAY_POINT))
                ));
    }

    public Set<WorkAbsenceState> absencesForWorker(Long workerId) {
        return absences.values().stream()
                .filter(a -> Objects.equals(a.getWorkerId(), workerId))
                .collect(Collectors.toSet());
    }

    public Set<WorkAbsenceState> absencesAt(Instant t) {
        return absences.values().stream()
                .filter(a -> a.getAllowedInterval().contains(t))
                .collect(Collectors.toSet());
    }

    /**
     * @param foremanId id бригадира
     * @param t момент времени
     * @return ВСЕ пропуски для людей, подчиненных на некоторый заданный момент данному бригадиру
     */
    public Set<WorkAbsenceState> allAbsencesForSubordinates(Long foremanId, Instant t) {
        return subordinatesAt(foremanId, t).stream()
                .map(w -> absencesForWorker(w.getId()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * @param foremanId id бригадира
     * @param t момент времени
     * @return список подчиненных данного бригадира, которым на данный момент времени выдан отгул
     */
    public Set<WorkAbsenceState> absencesForSubordinates(Long foremanId, Instant t) {
        return absencesAt(t).stream()
                .filter(absence -> isSubordinateAt(absence.getWorkerId(), foremanId, t))
                .collect(Collectors.toSet());
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