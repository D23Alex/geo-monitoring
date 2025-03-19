package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.AbsenceReason;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class WorkAbsenceState {
    WorkerState worker;
    Instant absenceRequestedFrom;
    Instant absenceRequestedTo;
    AbsenceReason absenceReason;
    String reasonComment;

    Instant absenceAllowedFrom;
    Instant absenceAllowedTo;
    boolean isAbsenceAllowed;
    String verdictComment;
}
