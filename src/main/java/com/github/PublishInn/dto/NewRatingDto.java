package com.github.PublishInn.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewRatingDto {
    private Long workId;
    private short rate;
}
