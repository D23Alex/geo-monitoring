package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final EventRepository eventRepository;

    public ReportController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Получение статистики по задачам за указанный период (ADMIN)
    @GetMapping("/tasks")
    public Map<String, Long> getTaskStatistics(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        SystemState state = eventRepository.findAll().stream()
                .reduce(SystemState.initial(),
                        (s, e) -> e.updateState(s),
                        (s1, s2) -> s2);

        long totalTasks = state.getTasks().stream()
                .filter(task -> task.getCreatedAt().isAfter(from) && task.getCreatedAt().isBefore(to))
                .count();

        long completedTasks = state.getTasks().stream()
                .filter(task -> task.getStatus().name().equals("COMPLETED"))
                .filter(task -> task.getClosedAt() != null &&
                        task.getClosedAt().isAfter(from) &&
                        task.getClosedAt().isBefore(to))
                .count();

        return Map.of("totalTasks", totalTasks, "completedTasks", completedTasks);
    }

    // Отчёт о прогрессе выполнения плана – распределение задач по статусам
    @GetMapping("/progress")
    public Map<String, Object> getProgressReport() {
        SystemState state = eventRepository.findAll().stream()
                .reduce(SystemState.initial(),
                        (s, e) -> e.updateState(s),
                        (s1, s2) -> s2);

        Map<String, Long> statusCount = state.getTasks().stream()
                .collect(Collectors.groupingBy(task -> task.getStatus().name(), Collectors.counting()));

        return Map.of("progress", statusCount);
    }
}
