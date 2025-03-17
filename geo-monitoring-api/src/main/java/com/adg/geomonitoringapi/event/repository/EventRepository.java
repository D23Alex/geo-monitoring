package com.adg.geomonitoringapi.event.repository;

import com.adg.geomonitoringapi.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByTimestampAsc();

    List<Event> findAllByTimestampAfterOrderByTimestampAsc(Instant timestampAfter);

    List<Event> findAllByTimestampBeforeOrderByTimestampAsc(Instant timestampBefore);

    List<Event> findAllByTimestampBetweenOrderByTimestampAsc(Instant timestampAfter, Instant timestampBefore);
}
