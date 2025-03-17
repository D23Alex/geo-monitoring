package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.worker.dto.WorkerCreationDTO;
import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.worker.mapper.WorkerMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkerMapperTest {

    @Autowired
    private WorkerMapper workerMapper;

    @Test
    public void testToEntityFromCreationDTO() {
        // Создаем DTO для создания работника
        WorkerCreationDTO creationDTO = new WorkerCreationDTO();
        creationDTO.setName("John Doe");

        // Преобразуем DTO в сущность
        Worker worker = workerMapper.toEntityFromCreationDTO(creationDTO);

        // Проверяем, что маппинг выполнен корректно
        Assertions.assertThat(worker).isNotNull();
        Assertions.assertThat(worker.getName()).isEqualTo("John Doe");
        Assertions.assertThat(worker.getId()).isNull();
    }

    @Test
    public void testToResponseDTO() {
        // Создаем сущность работника
        Worker worker = new Worker();
        worker.setId(1L);
        worker.setName("Jane Doe");

        // Преобразуем сущность в DTO для ответа
        WorkerResponseDTO responseDTO = workerMapper.toResponseDTO(worker);

        // Проверяем, что маппинг выполнен корректно
        Assertions.assertThat(responseDTO).isNotNull();
        Assertions.assertThat(responseDTO.getId()).isEqualTo(1L);
        Assertions.assertThat(responseDTO.getName()).isEqualTo("Jane Doe");
    }

    @Test
    public void testToEntityFromResponseDTO() {
        // Создаем DTO для ответа
        WorkerResponseDTO responseDTO = new WorkerResponseDTO();
        responseDTO.setId(2L);
        responseDTO.setName("Alice Smith");

        // Преобразуем DTO в сущность
        Worker worker = workerMapper.toEntityFromResponseDTO(responseDTO);

        // Проверяем корректность маппинга
        Assertions.assertThat(worker).isNotNull();
        Assertions.assertThat(worker.getId()).isEqualTo(2L);
        Assertions.assertThat(worker.getName()).isEqualTo("Alice Smith");
    }

    @Test
    public void testToCreationDTO() {
        // Создаем сущность работника
        Worker worker = new Worker();
        worker.setId(3L);
        worker.setName("Bob Johnson");

        // Преобразуем сущность в DTO для создания
        WorkerCreationDTO creationDTO = workerMapper.toCreationDTO(worker);

        // Проверяем корректность маппинга
        Assertions.assertThat(creationDTO).isNotNull();
        Assertions.assertThat(creationDTO.getName()).isEqualTo("Bob Johnson");
    }

    @Test
    public void testNullMapping() {
        // Проверяем, что при передаче null возвращается null
        Assertions.assertThat(workerMapper.toEntityFromCreationDTO(null)).isNull();
        Assertions.assertThat(workerMapper.toResponseDTO(null)).isNull();
        Assertions.assertThat(workerMapper.toEntityFromResponseDTO(null)).isNull();
        Assertions.assertThat(workerMapper.toCreationDTO(null)).isNull();
    }
}
