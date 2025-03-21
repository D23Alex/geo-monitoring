package com.adg.geomonitoringapi.mapper;

import com.adg.geomonitoringapi.event.Point;
import com.adg.geomonitoringapi.state.dto.PointDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Маппинг для Point в PointDTO
        modelMapper.addMappings(new PropertyMap<Point, PointDTO>() {
            @Override
            protected void configure() {
                map(source.getLatitude(), destination.getLatitude());
                map(source.getLongitude(), destination.getLongitude());
            }
        });

        return modelMapper;
    }
}
