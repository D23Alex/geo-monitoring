package com.adg.geomonitoringapi.util;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Interval {
    private Instant start;
    private Instant end;

    public Interval(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time" + start + end);
        }

        this.start = start;
        this.end = end;
    }

    public boolean contains(Instant t) {
        return t.isAfter(start) && t.isBefore(end);
    }

    public boolean contains(Interval other) {
        return start.isBefore(other.getEnd()) && end.isAfter(other.getEnd());
    }

    public boolean isContainedBy(Interval other) {
        return other.contains(this);
    }

    public boolean overlaps(Interval other) {
        return start.isBefore(other.getEnd()) && other.getStart().isBefore(end);
    }

    public long toDurationInSeconds() {
        return java.time.Duration.between(start, end).getSeconds();
    }
}
