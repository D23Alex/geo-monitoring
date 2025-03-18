package com.adg.geomonitoringapi.event.entity;

import com.adg.geomonitoringapi.event.AbsenceReason;
import com.adg.geomonitoringapi.state.SystemState;
import com.adg.geomonitoringapi.state.WorkAbsenceState;
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
public class WorkAbsenceRequestEvent extends Event {
    Instant absenceFrom;
    Instant absenceTo;
    AbsenceReason absenceReason;
    String reasonComment;

    @Override
    public SystemState apply(SystemState oldState) {
        Long newAbsenceId = getId();
        if (oldState.getAbsences().containsKey(newAbsenceId))
            throw new SystemState.StateUpdateException("Невозможно создать пропуск работы: пропуск работы с id "
                    + newAbsenceId + " уже существует");

        var newAbsences = new HashMap<>(oldState.getAbsences());
        newAbsences.put(newAbsenceId,
                WorkAbsenceState.builder()
                        .absenceRequestedTo(absenceTo)
                        .absenceRequestedFrom(absenceFrom)
                        .absenceReason(absenceReason)
                        .reasonComment(reasonComment).build());

        return oldState.withAbsences(newAbsences);
    }
}
