package com.adg.geomonitoringapi.state.service;

import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.util.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final StateService stateService;

    /**
     * Метод группирует задачи по локациям и интервалам времени для конкретного рабочего.
     * @param workerId id рабочего.
     * @param t момент времени, на который нужно группировать задачи.
     * @return Карта с локациями и интервалами времени, содержащая задачи.
     */
    public Map<LocationState, Map<Interval, Set<TaskState>>> groupTasksByLocationAndInterval(Long workerId, Instant t) {
        SystemState systemState = stateService.getLatestState();

        Set<TaskState> tasksForWorker = systemState.tasksForWorker(workerId).stream()
                .filter(task -> task.isActive(t))
                .collect(Collectors.toSet());

        return tasksForWorker.stream()
                .collect(Collectors.groupingBy(
                        task -> systemState.getLocations().get(task.getLocationId()),
                        Collectors.groupingBy(
                                TaskState::getActiveInterval,
                                Collectors.toSet()
                        )
                ));
    }
}
