package com.github.PublishInn.dto;

import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkDetailsDto {
    private String title;
    private WorkType type;
    private Long userId;
    private String text;
    private BigDecimal rating;
    private WorkStatus status;
}
