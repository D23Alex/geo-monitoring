package com.adg.geomonitoringapi.state;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class CompletionCriteriaState {
    private String name;
    private String description;
    private boolean isCompleted;
    private String comment;
    private Long photoProofId;
}
