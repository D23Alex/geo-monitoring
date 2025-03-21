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
        return switch (eventType.toLowerCase()) {
            case "locationcreation" -> LocationCreationEvent.class;
            case "abnormalsituation" -> AbnormalSituationEvent.class;
            case "taskassigned" -> TaskAssignedEvent.class;
            case "taskcancelled" -> TaskCancelledEvent.class;
            case "taskcompleted" -> TaskCompletedEvent.class;
            case "workergroupcreation" -> WorkerGroupCreationEvent.class;
            case "workerpositionupdate" -> WorkerPositionUpdateEvent.class;
            case "criteriacompleted" -> CriteriaCompletedEvent.class;
            default -> throw new UnsupportedEventType("Event type is not exists");
        };
    }

    public EventResponseDTO mapEventToResponseDTO(Event event) {
        return getEventResponseDTO(event, modelMapper);
    }

    public static EventResponseDTO getEventResponseDTO(Event event, ModelMapper modelMapper) {
        Class<? extends EventResponseDTO> responseDTOClass = switch (event.getClass().getSimpleName()) {
            case "LocationCreationEvent" -> LocationCreationEventResponseDTO.class;
            case "AbnormalSituationEvent" -> AbnormalSituationEventResponseDTO.class;
            case "TaskAssignedEvent" -> TaskAssignedEventResponseDTO.class;
            case "TaskCancelledEvent" -> TaskCancelledEventResponseDTO.class;
            case "TaskCompletedEvent" -> TaskCompletedEventResponseDTO.class;
            case "WorkerGroupCreationEvent" -> WorkerGroupCreationEventResponseDTO.class;
            case "WorkerPositionUpdateEvent" -> WorkerPositionUpdateEventResponseDTO.class;
            case "CriteriaCompletedEvent" -> CriteriaCompletedEventResponseDTO.class;
            default -> throw new UnsupportedDtoException("Event type is not exists");
        };

        return modelMapper.map(event, responseDTOClass);
    }
}
