package com.adg.geomonitoringapi.event.factory;

import com.adg.geomonitoringapi.event.dto.*;
import com.adg.geomonitoringapi.event.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventFactory {

    private final ModelMapper modelMapper = new ModelMapper();

    public Event createEvent(EventCreationDTO eventCreationDTO) {
        if (eventCreationDTO instanceof LocationCreationEventCreationDTO locationCreationDTO) {
            return modelMapper.map(locationCreationDTO, LocationCreationEvent.class);
        }
        if (eventCreationDTO instanceof AbnormalSituationEventCreationDTO abnormalDTO) {
            return modelMapper.map(abnormalDTO, AbnormalSituationEvent.class);
        }
        if (eventCreationDTO instanceof TaskAssignedEventCreationDTO taskAssignedDTO) {
            return modelMapper.map(taskAssignedDTO, TaskAssignedEvent.class);
        }
        if (eventCreationDTO instanceof TaskCancelledEventCreationDTO taskCancelledEventCreationDTO) {
            return modelMapper.map(taskCancelledEventCreationDTO, TaskCancelledEvent.class);
        }
        if (eventCreationDTO instanceof TaskCompletedEventCreationDTO taskCompletedEventCreationDTO) {
            return modelMapper.map(taskCompletedEventCreationDTO, TaskCompletedEvent.class);
        }
        if (eventCreationDTO instanceof WorkerGroupCreationEventCreationDTO workerGroupCreationEventCreationDTO) {
            return modelMapper.map(workerGroupCreationEventCreationDTO, WorkerGroupCreationEvent.class);
        }
        // Добавляем другие типы событий здесь, если нужно
        throw new IllegalArgumentException("Unsupported Event DTO type: " + eventCreationDTO.getClass().getName());
    }
}