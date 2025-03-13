package com.adg.geomonitoringapi.worker.repository;

import com.adg.geomonitoringapi.event.entity.WorkerGroupCreationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerGroupCreationEventRepository extends JpaRepository<WorkerGroupCreationEvent, Long> {
}
