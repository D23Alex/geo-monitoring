package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.worker.dto.WorkerCreationDTO;
import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.adg.geomonitoringapi.worker.entity.Worker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkerMapperTest {

    private final ModelMapper mapper = new ModelMapper();
    // Тесты для WorkerCreationMapper

    @Test
    public void testWorkerCreationMapper_toDto() {
        // Подготовка данных
        Worker worker = new Worker();
        worker.setId(1L);
        worker.setName("John Doe");

        // Вызов маппера
        WorkerCreationDTO creationDTO = mapper.map(worker, WorkerCreationDTO.class);
        System.out.println(creationDTO.getName());

        // Проверка результата
        Assertions.assertThat(creationDTO).isNotNull();
        Assertions.assertThat(creationDTO.getName()).isEqualTo("John Doe");
    }

    @Test
    public void testWorkerCreationMapper_toEntity() {
        // Подготовка данных
        WorkerCreationDTO creationDTO = new WorkerCreationDTO();
        creationDTO.setName("Jane Doe");

        // Вызов маппера
        Worker worker = mapper.map(creationDTO, Worker.class);

        // Проверка результата
        Assertions.assertThat(worker).isNotNull();
        Assertions.assertThat(worker.getName()).isEqualTo("Jane Doe");
    }

    // Тесты для WorkerResponseMapper

    @Test
    public void testWorkerResponseMapper_toDto() {
        // Подготовка данных
        Worker worker = new Worker();
        worker.setId(2L);
        worker.setName("Alice Smith");

        // Вызов маппера
        WorkerResponseDTO responseDTO = mapper.map(worker, WorkerResponseDTO.class);

        // Проверка результата
        Assertions.assertThat(responseDTO).isNotNull();
        Assertions.assertThat(responseDTO.getId()).isEqualTo(2L);
        Assertions.assertThat(responseDTO.getName()).isEqualTo("Alice Smith");
    }

    @Test
    public void testWorkerResponseMapper_toEntity() {
        // Подготовка данных
        WorkerResponseDTO responseDTO = new WorkerResponseDTO();
        responseDTO.setId(3L);
        responseDTO.setName("Bob Johnson");

        // Вызов маппера
        Worker worker = mapper.map(responseDTO, Worker.class);

        // Проверка результата
        Assertions.assertThat(worker).isNotNull();
        Assertions.assertThat(worker.getId()).isEqualTo(3L);
        Assertions.assertThat(worker.getName()).isEqualTo("Bob Johnson");
    }
}