package com.adg.geomonitoringapi.worker.service;

import com.adg.geomonitoringapi.worker.dto.WorkerCreationDTO;
import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.worker.mapper.WorkerMapper;
import org.mapstruct.factory.Mappers;

public class Main {
    public static void main(String[] args) {
        WorkerMapper workerMapper = Mappers.getMapper(WorkerMapper.class);

        Worker worker = new Worker(1L, "John Doe");

        // Преобразуем Worker в WorkerCreationDTO
        WorkerCreationDTO workerCreationDTO = workerMapper.toCreationDTO(worker);
        System.out.println("WorkerCreationDTO: " + workerCreationDTO.getName());

        // Преобразуем Worker в WorkerResponseDTO
        WorkerResponseDTO workerResponseDTO = workerMapper.toResponseDTO(worker);
        System.out.println("WorkerResponseDTO: " + workerResponseDTO.getName());

        // Преобразуем WorkerCreationDTO обратно в Worker
        Worker workerFromCreationDTO = workerMapper.toEntityFromCreationDTO(workerCreationDTO);
        System.out.println("Worker from WorkerCreationDTO: " + workerFromCreationDTO.getName());

        // Преобразуем WorkerResponseDTO обратно в Worker
        Worker workerFromResponseDTO = workerMapper.toEntityFromResponseDTO(workerResponseDTO);
        System.out.println("Worker from WorkerResponseDTO: " + workerFromResponseDTO.getName());
    }
}




