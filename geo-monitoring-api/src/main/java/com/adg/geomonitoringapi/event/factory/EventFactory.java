package com.adg.geomonitoringapi.event.factory;

import com.adg.geomonitoringapi.event.dto.EventCreationDTO;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.exception.UnsupportedEventType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventFactory {

    private final ModelMapper modelMapper = new ModelMapper();

    private static final Map<String, Class<? extends Event>> eventMap = new HashMap<>();

    static {
        eventMap.put("location-creations", LocationCreationEvent.class);
        eventMap.put("abnormal-situations", AbnormalSituationEvent.class);
        eventMap.put("task-assignments", TaskAssignedEvent.class);
        eventMap.put("task-cancellations", TaskCancelledEvent.class);
        eventMap.put("task-completions", TaskCompletedEvent.class);
        eventMap.put("worker-group-creations", WorkerGroupCreationEvent.class);
    }

    /**
     * Создает событие по типу eventType.
     * @param eventType Тип события.
     * @param eventCreationDTO Данные для создания события.
     * @return Созданное событие.
     */
    public Event createEvent(String eventType, EventCreationDTO eventCreationDTO) {
        Class<? extends Event> eventClass = eventMap.get(eventType.toLowerCase());
        if (eventClass == null) {
            throw new UnsupportedEventType("Event type is not supported");
        }
        return modelMapper.map(eventCreationDTO, eventClass);
    }
}