package com.github.PublishInn.unitTests.mappers;

import com.github.PublishInn.dto.NewRatingDto;
import com.github.PublishInn.dto.RatingDetailsDto;
import com.github.PublishInn.dto.mappers.RatingMapper;
import com.github.PublishInn.model.entity.Rating;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingMapperTest {
    private final RatingMapper mapper = Mappers.getMapper(RatingMapper.class);
    private final Rating rating = new Rating();
    private final NewRatingDto ratingDto = new NewRatingDto(4L, (short) 9);

    @BeforeAll
    void setup() {
        rating.setWorkId(2L);
        rating.setUserId(3L);
        rating.setId(5L);
        rating.setRate((short) 6);
    }

    @Test
    void toDtoTest() {
        RatingDetailsDto dto = mapper.fromRating(rating);

        assertThat(dto.getWorkId()).isEqualTo(rating.getWorkId());
        assertThat(dto.getRate()).isEqualTo(rating.getRate());
    }

    @Test
    void toRatingTest() {
        Rating toRating = mapper.fromNewRatingDto(ratingDto);

        assertThat(toRating.getRate()).isEqualTo(ratingDto.getRate());
        assertThat(toRating.getWorkId()).isEqualTo(ratingDto.getWorkId());
    }
}
