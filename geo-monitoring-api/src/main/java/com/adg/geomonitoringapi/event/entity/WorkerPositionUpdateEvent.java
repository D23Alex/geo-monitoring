package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class WorkerPositionUpdateEvent extends Event {
    private Long workerId;
    private Double latitude;
    private Double longitude;
    private Instant updateTime;

    @Override
    public SystemState updateState(SystemState oldState) {
        // В данной реализации состояние системы (SystemState) не хранит детальную информацию о позициях.
        // Обновление геопозиции можно обрабатывать отдельным сервисом, обновляя WorkerState.
        return oldState;
    }
}
