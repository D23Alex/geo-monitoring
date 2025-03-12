package com.adg.geomonitoringapi.repository;

import com.adg.geomonitoringapi.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
