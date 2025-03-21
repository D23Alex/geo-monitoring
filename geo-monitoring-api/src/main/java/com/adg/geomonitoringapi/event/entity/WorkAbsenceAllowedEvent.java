package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkAbsenceState;
import com.adg.geomonitoringapi.util.Interval;
import com.adg.geomonitoringapi.util.Util;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkAbsenceAllowedEvent extends Event {
    //TODO: возможно дополнить в зависимости от того, кто выносит вердикт: бригадир или админ
    private Long absenceId;
    Instant absenceAllowedFrom;
    Instant absenceAllowedTo;
    boolean isAbsenceAllowed;
    String verdictComment;


    @Override
    public Set<String> oldStateIssues(SystemState oldState) {
        return Util.construct(Map.of(
                () -> !oldState.getAbsences().containsKey(absenceId),
                "Невозможно одобрить пропуск работы: пропуск работы с id " + absenceId + " не существует"
        ));
    }

    @Override
    public SystemState apply(SystemState oldState) {
        WorkAbsenceState newAbsence = oldState.getAbsences().get(absenceId)
                .withAbsenceAllowed(true)
                .withVerdictComment(verdictComment);

        Instant from = absenceAllowedFrom;
        Instant to = absenceAllowedTo;

        if (absenceAllowedTo != null)
            to = absenceAllowedTo;
        if (absenceAllowedFrom != null)
            from = absenceAllowedFrom;

        newAbsence.setAllowedInterval(new Interval(from, to));

        var newAbsences = new HashMap<>(oldState.getAbsences());
        newAbsences.put(absenceId, newAbsence);

        return oldState.withAbsences(newAbsences);
    }
}
