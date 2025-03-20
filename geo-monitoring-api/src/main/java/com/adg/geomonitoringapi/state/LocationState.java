package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.Point;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class LocationState {
    private Long id;
    private List<Point> points;
    private String name;
}
