package com.adg.geomonitoringapi.event.factory;

import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.exception.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventFactory {

    private final ModelMapper modelMapper = new ModelMapper();

    private static final Map<Class<? extends EventCreationDTO>, Class<? extends Event>> eventMap = new HashMap<>();

    static {
        eventMap.put(LocationCreationEventCreationDTO.class, LocationCreationEvent.class);
        eventMap.put(AbnormalSituationEventCreationDTO.class, AbnormalSituationEvent.class);
        eventMap.put(TaskAssignedEventCreationDTO.class, TaskAssignedEvent.class);
        eventMap.put(TaskCancelledEventCreationDTO.class, TaskCancelledEvent.class);
        eventMap.put(TaskCompletedEventCreationDTO.class, TaskCompletedEvent.class);
        eventMap.put(WorkerGroupCreationEventCreationDTO.class, WorkerGroupCreationEvent.class);
    }

    public Event createEvent(EventCreationDTO eventCreationDTO) {
        Class<? extends Event> eventClass = eventMap.get(eventCreationDTO.getClass());
        if (eventClass == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        return modelMapper.map(eventCreationDTO, eventClass);
    }
}
