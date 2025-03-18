package com.adg.geomonitoringapi.event;

import com.adg.geomonitoringapi.event.entity.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class EventConfig {
    @Bean
    public Queue<Event> newEventsQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
