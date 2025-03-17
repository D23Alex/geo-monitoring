package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.Point;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class LocationState {
    private Set<Point> points;
    private String name;
}
