package com.adg.geomonitoringapi.event;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Point {
    private Double latitude;
    private Double longitude;
}
