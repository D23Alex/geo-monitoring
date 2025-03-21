package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.event.Worker;
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
public class AbnormalSituationEvent extends Event {
    private Worker worker;
    private String description; // описание внештатной ситуации

    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Set.of();
    }

    @Override
    public SystemState apply(SystemState oldState) {
        // В данной простой реализации состояние не изменяется,
        // но в полном варианте можно добавить список событий с внештатными ситуациями в state.
        return oldState;
    }
}
