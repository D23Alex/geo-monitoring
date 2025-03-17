package com.adg.geomonitoringapi.worker.controller;

import com.adg.geomonitoringapi.worker.dto.WorkerCreationDTO;
import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.worker.mapper.WorkerMapper;
import com.adg.geomonitoringapi.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;
    private final WorkerMapper workerMapper;

    @PostMapping
    public ResponseEntity<WorkerResponseDTO> createWorker(@RequestBody WorkerCreationDTO workerCreationDTO) {
        Worker worker = workerMapper.toEntityFromCreationDTO(workerCreationDTO);
        Worker createdWorker = workerService.createWorker(worker);
        WorkerResponseDTO workerResponseDTO = workerMapper.toResponseDTO(createdWorker);
        return ResponseEntity.created(URI.create("/api/workers/" + worker.getId())).body(workerResponseDTO);
    }
}
