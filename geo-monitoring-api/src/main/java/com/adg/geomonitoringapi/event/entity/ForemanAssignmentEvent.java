package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ForemanAssignmentEvent extends Event { //TODO: дублирует WorkerGroupCreationEvent, удалить
    @ManyToOne
    private Worker worker;
    // Идентификаторы подчинённых, за которыми назначается бригадир
    @OneToMany
    private Set<Worker> subordinateWorkers;

    @Override
    public SystemState updateState(SystemState oldState) {
        // В данной реализации не изменяем SystemState,
        // а назначение бригадиром обрабатывается отдельно (например, update in place для сущности Worker).
        return oldState;
    }
}
