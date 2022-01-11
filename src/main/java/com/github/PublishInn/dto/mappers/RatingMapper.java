package com.github.PublishInn.dto.mappers;

import com.github.PublishInn.dto.NewRatingDto;
import com.github.PublishInn.dto.RatingDetailsDto;
import com.github.PublishInn.model.entity.Rating;
import org.mapstruct.Mapper;

@Mapper
public interface RatingMapper {
    Rating fromNewRatingDto(NewRatingDto newRatingDto);
    RatingDetailsDto fromRating(Rating rating);
}
