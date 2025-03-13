package com.adg.geomonitoringapi.snapshot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant snapshotTime;

    // Храним сериализованное состояние системы (например, в формате JSON)
    @Lob
    private String stateJson;
}
