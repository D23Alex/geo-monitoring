package com.adg.geomonitoringapi.event.mapper;

import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.*;
import com.adg.geomonitoringapi.exception.UnsupportedDtoException;
import com.adg.geomonitoringapi.exception.UnsupportedEventType;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public Class<? extends Event> getEventClassFromString(String eventType) {
        return getaClass(eventType);
    }

    @NotNull
    public static Class<? extends Event> getaClass(String eventType) {
        switch (eventType.toLowerCase()) {
            case "location-creations": return LocationCreationEvent.class;
            case "abnormal-situations": return AbnormalSituationEvent.class;
            case "task-assignments": return TaskAssignedEvent.class;
            case "task-cancellations": return TaskCancelledEvent.class;
            case "task-completions": return TaskCompletedEvent.class;
            case "worker-group-creations": return WorkerGroupCreationEvent.class;
            case "worker-position-updates": return WorkerPositionUpdateEvent.class;
            case "criteria-completions": return CriteriaCompletedEvent.class;
            default: throw new UnsupportedEventType("Event type is not exists");
        }
    }

    public EventResponseDTO mapEventToResponseDTO(Event event) {
        return getEventResponseDTO(event, modelMapper);
    }

    public static EventResponseDTO getEventResponseDTO(Event event, ModelMapper modelMapper) {
        if (event instanceof LocationCreationEvent) {
            return modelMapper.map(event, LocationCreationEventResponseDTO.class);
        } else if (event instanceof AbnormalSituationEvent) {
            return modelMapper.map(event, AbnormalSituationEventResponseDTO.class);
        } else if (event instanceof TaskAssignedEvent) {
            return modelMapper.map(event, TaskAssignedEventResponseDTO.class);
        } else if (event instanceof TaskCancelledEvent) {
            return modelMapper.map(event, TaskCancelledEventResponseDTO.class);
        } else if (event instanceof TaskCompletedEvent) {
            return modelMapper.map(event, TaskCompletedEventResponseDTO.class);
        } else if (event instanceof WorkerGroupCreationEvent) {
            return modelMapper.map(event, WorkerGroupCreationEventResponseDTO.class);
        } else if (event instanceof WorkerPositionUpdateEvent) {
            return modelMapper.map(event, WorkerPositionUpdateEventResponseDTO.class);
        } else if (event instanceof CriteriaCompletedEvent) {
            return modelMapper.map(event, CriteriaCompletedEventResponseDTO.class);
        } else {
            throw new UnsupportedDtoException("Event type is not exists");
        }
    }
}
