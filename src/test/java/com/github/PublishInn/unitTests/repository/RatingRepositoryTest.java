package com.github.PublishInn.unitTests.repository;

import com.github.PublishInn.model.entity.Rating;
import com.github.PublishInn.model.repository.RatingRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;
    private final Rating rating = new Rating();
    private final Rating rating2 = new Rating();

    @BeforeAll
    void setup() {
        rating.setUserId(2L);
        rating.setWorkId(3L);
        rating.setRate((short) 8);
        rating.setCreatedBy(2L);

        rating2.setUserId(2L);
        rating2.setWorkId(1L);
        rating2.setRate((short) 7);
        rating2.setCreatedBy(2L);

        ratingRepository.save(rating);
        ratingRepository.save(rating2);
    }

    @Test
    void findRatingsByUserIdTest() {
        List<Rating> ratings = ratingRepository.findRatingsByUserId(2L);

        assertThat(ratings.size()).isEqualTo(2);
        assertThat(ratings.stream()
                .filter(rating1 -> rating1.getUserId() == 2L && rating1.getWorkId() == 3L)
                .findAny())
                .isPresent();
        assertThat(ratings.stream()
                .filter(rating1 -> rating1.getUserId() == 2L && rating1.getWorkId() == 1L)
                .findAny())
                .isPresent();
    }

    @Test
    void findRatingByUserIdAndWorkIdTest() {
        Optional<Rating> ratingOptional = ratingRepository.findRatingByUserIdAndWorkId(2L, 1L);

        assertThat(ratingOptional.isPresent()).isTrue();
        assertThat(ratingOptional.get().getRate()).isEqualTo((short) 7);

        Optional<Rating> ratingEmpty = ratingRepository.findRatingByUserIdAndWorkId(3L, 3L);
        assertThat(ratingEmpty.isPresent()).isFalse();
    }
}
