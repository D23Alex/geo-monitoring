package com.adg.geomonitoringapi.state.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
    private Double latitude;
    private Double longitude;
}
