package com.github.PublishInn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class NewRatingDto {
    @NotNull
    private Long workId;
    @NotNull
    private short rate;
}
