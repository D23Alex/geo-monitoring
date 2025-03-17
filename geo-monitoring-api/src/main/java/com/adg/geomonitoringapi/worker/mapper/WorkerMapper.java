package com.adg.geomonitoringapi.worker.mapper;

import com.adg.geomonitoringapi.mapper.BaseMapper;
import com.adg.geomonitoringapi.worker.dto.WorkerCreationDTO;
import com.adg.geomonitoringapi.worker.dto.WorkerResponseDTO;
import com.adg.geomonitoringapi.worker.entity.Worker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkerMapper extends BaseMapper<WorkerCreationDTO, WorkerResponseDTO, Worker> {
}