package com.adg.geomonitoringapi.snapshot.repository;

import com.adg.geomonitoringapi.snapshot.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
}
