package com.adg.geomonitoringapi.repository;

import com.adg.geomonitoringapi.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByTimestampAsc();
}
