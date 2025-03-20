package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkAbsenceState;
import com.adg.geomonitoringapi.util.Interval;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;

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
    public SystemState apply(SystemState oldState) {
        if (oldState.getAbsences().containsKey(absenceId))
            throw new SystemState.StateUpdateException("Невозможно одобрить пропуск работы: пропуск работы с id "
                    + absenceId + " не существует");

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
