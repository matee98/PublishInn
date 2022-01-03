package com.github.PublishInn.dto;

import com.github.PublishInn.model.entity.enums.WorkType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkSaveDto {
    private String title;
    private WorkType type;
    private String text;
}
