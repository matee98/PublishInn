package com.github.PublishInn.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
public class NewRatingDto {
    @NotNull
    private Long workId;
    @NotNull
    private short rate;
}
