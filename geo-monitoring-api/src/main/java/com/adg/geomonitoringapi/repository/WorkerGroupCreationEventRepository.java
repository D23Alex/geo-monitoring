package com.adg.geomonitoringapi.repository;

import com.adg.geomonitoringapi.event.WorkerGroupCreationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerGroupCreationEventRepository extends JpaRepository<WorkerGroupCreationEvent, Long> {
}
