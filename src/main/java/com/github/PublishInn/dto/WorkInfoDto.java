package com.github.PublishInn.dto;

import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkInfoDto {
    private Long id;
    private String title;
    private WorkType type;
    private String username;
    private BigDecimal rating;
    private WorkStatus status;
    private LocalDateTime createdOn;
}
