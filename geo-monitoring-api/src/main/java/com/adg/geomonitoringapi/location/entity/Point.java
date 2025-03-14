package com.adg.geomonitoringapi.location.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int width;
    private int length;
}
