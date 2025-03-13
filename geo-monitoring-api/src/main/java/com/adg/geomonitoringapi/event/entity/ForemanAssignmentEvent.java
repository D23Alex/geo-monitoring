package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForemanAssignmentEvent extends Event {
    private Long workerId;
    // Идентификаторы подчинённых, за которыми назначается бригадир
    private Set<Long> subordinateWorkerIds;

    @Override
    public SystemState updateState(SystemState oldState) {
        // В данной реализации не изменяем SystemState,
        // а назначение бригадиром обрабатывается отдельно (например, update in place для сущности Worker).
        return oldState;
    }
}
