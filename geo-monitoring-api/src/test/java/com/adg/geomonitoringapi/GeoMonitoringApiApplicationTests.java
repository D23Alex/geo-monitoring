package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.event.TaskStatus;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.state.GroupState;
import com.adg.geomonitoringapi.state.LocationState;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.TaskState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.event.repository.EventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class GeoMonitoringApiApplicationTests {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @Test
    public void testSequentialEventsStateUpdate() {
        // Начальное состояние системы
        SystemState state = SystemState.initial();

        // 1. Событие создания локации
        LocationCreationEvent locationEvent = new LocationCreationEvent();
        locationEvent.setId(1L); // id используется как ключ для локации
        locationEvent.setName("Склад");
        locationEvent.setPoints(Set.of(new Point(10.0, 20.0), new Point(10.1, 20.1)));
        locationEvent.setTimestamp(Instant.now().minusSeconds(100));
        state = locationEvent.updateState(state);

        // Проверяем, что локация добавлена
        Assertions.assertThat(state.getLocations()).containsKey(1L);
        LocationState locState = state.getLocations().get(1L);
        Assertions.assertThat(locState.getName()).isEqualTo("Склад");

        // 2. Событие создания группы рабочих
        Worker foreman = new Worker();
        foreman.setId(100L);
        foreman.setName("Алиса");

        Worker worker = new Worker();
        worker.setId(101L);
        worker.setName("Боб");

        WorkerGroupCreationEvent groupEvent = new WorkerGroupCreationEvent();
        groupEvent.setId(2L); // ключ для группы
        groupEvent.setForeman(foreman);
        groupEvent.setWorkers(Set.of(worker));
        groupEvent.setGroupActiveFrom(Instant.now().minusSeconds(90));
        groupEvent.setGroupActiveTo(Instant.now().plusSeconds(90));
        groupEvent.setTimestamp(Instant.now().minusSeconds(90));
        state = groupEvent.updateState(state);

        // Проверяем, что группа добавлена
        Assertions.assertThat(state.getGroups()).containsKey(2L);
        GroupState groupState = state.getGroups().get(2L);
        Assertions.assertThat(groupState.getForeman()).isEqualTo(foreman);

        // 3. Событие назначения задачи (TaskAssignedEvent)
        TaskAssignedEvent taskAssignedEvent = new TaskAssignedEvent();
        taskAssignedEvent.setId(3L); // id задачи
        taskAssignedEvent.setLocationId(1L); // ссылка на существующую локацию
        taskAssignedEvent.setDescription("Проверка инвентаря");
//        taskAssignedEvent.setCompletionCriteria("Проверены все позиции");
        taskAssignedEvent.setActiveFrom(Instant.now().minusSeconds(80));
        taskAssignedEvent.setActiveTo(Instant.now().plusSeconds(80));
        taskAssignedEvent.setAssignedWorkers(Collections.emptySet());
        taskAssignedEvent.setTimestamp(Instant.now().minusSeconds(80));
        state = taskAssignedEvent.updateState(state);

        // Проверяем, что задача добавлена
        Assertions.assertThat(state.getTasks()).containsKey(3L);
        TaskState taskState = state.getTasks().get(3L);
        Assertions.assertThat(taskState.getDescription()).isEqualTo("Проверка инвентаря");
        Assertions.assertThat(taskState.getStatus()).isEqualTo(TaskStatus.CREATED);

        // 4. Событие завершения задачи (TaskCompletedEvent)
        TaskCompletedEvent taskCompletedEvent = new TaskCompletedEvent();
        taskCompletedEvent.setId(4L);
        taskCompletedEvent.setTaskId(3L); // завершаем задачу с id 3
        Instant closedAt = Instant.now();
        taskCompletedEvent.setClosedAt(closedAt);
        taskCompletedEvent.setTimestamp(Instant.now().minusSeconds(70));
        state = taskCompletedEvent.updateState(state);

        // Проверяем, что статус задачи изменился на COMPLETED и установлено время закрытия
        taskState = state.getTasks().get(3L);
        Assertions.assertThat(taskState.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        Assertions.assertThat(taskState.getClosedAt()).isEqualTo(closedAt);
    }

}