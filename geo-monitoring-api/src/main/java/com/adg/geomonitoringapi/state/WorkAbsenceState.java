package com.adg.geomonitoringapi.state;

import com.adg.geomonitoringapi.event.AbsenceReason;
import com.adg.geomonitoringapi.util.Interval;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class WorkAbsenceState {
    private Long id;

    Long workerId;
    Interval requestedInterval;
    AbsenceReason absenceReason;
    String reasonComment;
    Interval allowedInterval;
    boolean isAbsenceAllowed;
    String verdictComment;
}
