package com.adg.geomonitoringapi.event.repository;

import com.adg.geomonitoringapi.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByTimestampAsc();
}
