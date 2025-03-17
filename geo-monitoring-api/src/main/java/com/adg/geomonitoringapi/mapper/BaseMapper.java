package com.adg.geomonitoringapi.mapper;

public interface BaseMapper<D1, D2, E> {
    D1 toCreationDTO(E entity);
    D2 toResponseDTO(E entity);
    E toEntityFromCreationDTO(D1 dto);
    E toEntityFromResponseDTO(D2 dto);
}

