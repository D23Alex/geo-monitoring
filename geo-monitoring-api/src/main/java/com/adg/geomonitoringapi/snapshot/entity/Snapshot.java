package com.adg.geomonitoringapi.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant timestamp;

    // Храним сериализованное состояние системы (например, в формате JSON)
    @Lob
    private String stateJson;
}
