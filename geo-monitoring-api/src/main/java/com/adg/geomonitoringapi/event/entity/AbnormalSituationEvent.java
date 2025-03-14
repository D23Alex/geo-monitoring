package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.worker.entity.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class AbnormalSituationEvent extends Event {
    @ManyToOne
    private Worker worker;
    private String description; // описание внештатной ситуации

    @Override
    public SystemState updateState(SystemState oldState) {
        // В данной простой реализации состояние не изменяется,
        // но в полном варианте можно добавить список событий с внештатными ситуациями в state.
        return oldState;
    }
}
