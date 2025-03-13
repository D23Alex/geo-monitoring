package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class WorkerPositionUpdateEvent extends Event {
    private Long workerId;
    private double latitude;
    private double longitude;
    private Instant updateTime;

    @Override
    public SystemState updateState(SystemState oldState) {
        // В данной реализации состояние системы (SystemState) не хранит детальную информацию о позициях.
        // Обновление геопозиции можно обрабатывать отдельным сервисом, обновляя WorkerState.
        return oldState;
    }
}
