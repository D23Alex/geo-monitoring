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

    public Point interpolate(Point other, double ratio) {
        double lat = this.latitude + (other.latitude - this.latitude) * ratio;
        double lon = this.longitude + (other.longitude - this.longitude) * ratio;
        return new Point(lat, lon);
    }

    public static final Point SOME_FAR_AWAY_POINT = new Point(999.9, 999.9);
}
