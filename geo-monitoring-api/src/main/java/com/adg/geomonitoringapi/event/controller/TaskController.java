package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.event.entity.TaskAssignedEvent;
import com.adg.geomonitoringapi.event.entity.TaskCreatedEvent;
import com.adg.geomonitoringapi.event.entity.TaskCompletedEvent;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import com.adg.geomonitoringapi.state.SystemState;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final EventRepository eventRepository;

    public TaskController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Создание задачи (бригадир)
    @PostMapping
    public String createTask(@RequestBody TaskCreatedEvent event) {
        eventRepository.save(event);
        return "Task created event saved";
    }

    // Назначение задачи работнику
    @PostMapping("/{taskId}/assign")
    public String assignTask(@PathVariable Long taskId, @RequestBody TaskAssignedEvent event) {
        event.setTaskId(taskId);
        eventRepository.save(event);
        return "Task assigned event saved";
    }

    // Закрытие задачи (например, выполнена или отозвана)
    @PostMapping("/{taskId}/close")
    public String closeTask(@PathVariable Long taskId, @RequestBody TaskCompletedEvent event) {
        event.setTaskId(taskId);
        eventRepository.save(event);
        return "Task closed event saved";
    }

    // Получение состояния системы (в т.ч. список задач)
    @GetMapping
    public SystemState getTasksState() {
        return eventRepository.findAll().stream()
                .reduce(SystemState.initial(),
                        (state, event) -> event.updateState(state),
                        (s1, s2) -> s2);
    }
}
