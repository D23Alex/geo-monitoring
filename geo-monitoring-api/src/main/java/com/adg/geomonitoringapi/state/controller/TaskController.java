package com.adg.geomonitoringapi.state.controller;

import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.TaskState;
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
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/workers/{workerId}")
    public ResponseEntity<Map<LocationState, Map<Interval, Set<TaskState>>>> getGroupedTasksByLocationAndInterval(
            @PathVariable Long workerId,
            @RequestParam Instant timestamp) {
        Map<LocationState, Map<Interval, Set<TaskState>>> groupedTasks = taskService.groupTasksByLocationAndInterval(workerId, timestamp);
        return ResponseEntity.ok(groupedTasks);
    }
}
