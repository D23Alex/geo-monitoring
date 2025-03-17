package com.adg.geomonitoringapi.worker.mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkerMapperConfig {

    @Bean
    public WorkerMapper workerMapper() {
        return Mappers.getMapper(WorkerMapper.class);
    }
}
