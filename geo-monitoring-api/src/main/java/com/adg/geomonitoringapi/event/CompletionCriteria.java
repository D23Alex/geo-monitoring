package com.adg.geomonitoringapi.event;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CompletionCriteria {
    private boolean isCommentRequired;
    private boolean isPhotoProofRequired;
    private String name;
    private String description;
}
