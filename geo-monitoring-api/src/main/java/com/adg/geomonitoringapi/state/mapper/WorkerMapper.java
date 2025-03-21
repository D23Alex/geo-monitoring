package com.adg.geomonitoringapi.state.mapper;

import com.adg.geomonitoringapi.state.WorkerState;
import com.adg.geomonitoringapi.state.dto.PointDTO;
import com.adg.geomonitoringapi.state.dto.WorkerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WorkerMapper {

    private final ModelMapper modelMapper;

    public WorkerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public WorkerDTO toWorkerDTO(WorkerState workerState) {
        WorkerDTO workerDTO = modelMapper.map(workerState, WorkerDTO.class);

        Optional<PointDTO> lastKnownPosition = workerState.lastKnownPosition()
                .map(point -> modelMapper.map(point, PointDTO.class));

        workerDTO.setLastKnownPosition(lastKnownPosition);
        workerDTO.setDistanceTravelled(workerState.distanceTravelled());
        workerDTO.setIsIdle(workerState.isIdle());

        return workerDTO;
    }
}
