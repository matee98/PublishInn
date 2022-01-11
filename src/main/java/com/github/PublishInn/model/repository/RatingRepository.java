package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findRatingByUserIdAndWorkId(Long userId, Long workId);
}
