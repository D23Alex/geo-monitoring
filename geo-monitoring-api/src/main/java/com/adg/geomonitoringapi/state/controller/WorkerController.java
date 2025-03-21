package com.adg.geomonitoringapi.state.controller;

import com.adg.geomonitoringapi.exception.EntityNotFoundException;
import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.state.WorkerState;
import com.adg.geomonitoringapi.state.dto.WorkerDTO;
import com.adg.geomonitoringapi.state.mapper.WorkerMapper;
import com.adg.geomonitoringapi.state.service.StateServiceWithoutSnapshots;
import com.adg.geomonitoringapi.state.service.TaskService;
import com.adg.geomonitoringapi.util.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workers")
public class WorkerController {

    private final TaskService taskService;
    private final StateServiceWithoutSnapshots stateService;
    private final WorkerMapper workerMapper;


    /**
     * Получить задачи рабочего, сгруппированные по локации и интервалу времени.
     *
     * @param workerId ID рабочего.
     * @param timestamp Момент времени для получения задач.
     * @return Задачи, сгруппированные по локации и интервалу времени.
     */
    @GetMapping("/{workerId}/tasks")
    public ResponseEntity<Map<LocationState, Map<Interval, Set<TaskState>>>> getGroupedTasksByLocationAndInterval(
            @PathVariable Long workerId,
            @RequestParam Instant timestamp) {

        Map<LocationState, Map<Interval, Set<TaskState>>> groupedTasks = taskService.groupTasksByLocationAndInterval(workerId, timestamp);
        return ResponseEntity.ok(groupedTasks);
    }

    /**
     * Получает данные работника по его ID, включая последние известные данные о его местоположении.
     *
     * @param workerId ID работника, который требуется получить.
     * @return {@link ResponseEntity} с объектом {@link WorkerDTO}, содержащим информацию о работнике.
     *         Если работник не найден, возвращается код состояния 404 (Not Found).
     * @throws EntityNotFoundException Если работник с указанным ID не существует.
     */
    @GetMapping("/{workerId}")
    public ResponseEntity<WorkerDTO> getWorker(@PathVariable Long workerId) {
        WorkerState workerState = stateService.getLatestState().getWorkers().get(workerId);
        if (workerState == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        WorkerDTO workerDTO = workerMapper.toWorkerDTO(workerState);
        return ResponseEntity.ok(workerDTO);
    }

}
