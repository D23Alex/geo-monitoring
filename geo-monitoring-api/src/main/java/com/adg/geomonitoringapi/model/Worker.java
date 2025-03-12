package com.adg.geomonitoringapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Worker {
    @Id
    private Long id;
    private String name;
}
