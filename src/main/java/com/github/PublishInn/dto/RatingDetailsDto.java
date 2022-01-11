package com.github.PublishInn.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RatingDetailsDto {
    private String username;
    private Long workId;
    private short rate;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

}
