package com.adg.geomonitoringapi.worker.service;

import com.adg.geomonitoringapi.worker.entity.Worker;
import com.adg.geomonitoringapi.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkerService {
private final WorkerRepository workerRepository;

    @Transactional
    public Worker createWorker(Worker worker) {
        return workerRepository.save(worker);
    }

}
