package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
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
