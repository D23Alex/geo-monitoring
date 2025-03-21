package com.adg.geomonitoringapi.state.dto;

import com.adg.geomonitoringapi.event.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Long locationId;
    private List<Point> points;
    private String name;

}
