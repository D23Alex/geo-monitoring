package com.adg.geomonitoringapi.snapshot.repository;

import com.adg.geomonitoringapi.snapshot.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
    void deleteAllByTimestampAfter(Instant t);
    Optional<Snapshot> findTopByOrderByTimestamp();
    Optional<Snapshot> findTopByTimestampBeforeOrderByTimestamp(Instant t);
}
