package com.github.PublishInn.service;

import com.github.PublishInn.dto.NewRatingDto;
import com.github.PublishInn.dto.RatingDetailsDto;
import com.github.PublishInn.dto.mappers.RatingMapper;
import com.github.PublishInn.exceptions.RatingException;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.Rating;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.repository.RatingRepository;
import com.github.PublishInn.model.repository.UserRepository;
import com.github.PublishInn.model.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {

    private final static String USER_NOT_FOUND_MSG = "User with name %s not found";
    private final static String WORK_NOT_FOUND_MSG = "Work with id %s not found";
    private final static String RATING_NOT_FOUND_MSG = "Rating not found";

    private final RatingRepository ratingRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;


    public RatingDetailsDto getRating(String username, Long workId) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }
        RatingMapper mapper = Mappers.getMapper(RatingMapper.class);
        return ratingRepository.findRatingByUserIdAndWorkId(user.get().getId(), workId)
                .map(mapper::fromRating)
                .map(ratingDetailsDto -> {
                    ratingDetailsDto.setUsername(username);
                    return ratingDetailsDto;
                })
                .orElseThrow(NoSuchElementException::new);
    }

    public void addNewRating(@Valid NewRatingDto model, Principal principal) throws RatingException {
        RatingMapper mapper = Mappers.getMapper(RatingMapper.class);
        Rating rating = mapper.fromNewRatingDto(model);
        Optional<AppUser> user = userRepository.findByUsername(principal.getName());
        user.ifPresentOrElse(appUser -> {
            rating.setUserId(appUser.getId());
            rating.setCreatedBy(appUser.getId());
        }, () -> {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, principal.getName()));
        });
        Optional<Work> work = workRepository.findById(model.getWorkId());
        work.ifPresentOrElse(x -> rating.setWorkId(x.getId()), () -> {
            throw new NoSuchElementException(String.format(WORK_NOT_FOUND_MSG, model.getWorkId()));
        });
        if(ratingRepository.findRatingByUserIdAndWorkId(user.get().getId(), work.get().getId()).isPresent()) {
            throw RatingException.ratingAlreadyExists();
        }
        ratingRepository.save(rating);
        calculateWorkRating(rating.getWorkId());
    }

    public void updateRating(NewRatingDto model, Principal principal) {
        Optional<AppUser> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, principal.getName()));
        }
        Optional<Rating> rating = ratingRepository.findRatingByUserIdAndWorkId(user.get().getId(), model.getWorkId());
        if (rating.isPresent()) {
            rating.get().setRate(model.getRate());
            rating.get().setModifiedBy(user.get().getId());
            ratingRepository.save(rating.get());
        } else {
            throw new NoSuchElementException(RATING_NOT_FOUND_MSG);
        }
    }

    private void calculateWorkRating(Long workId) {
        Optional<Work> work = workRepository.findById(workId);
        if (work.isPresent()) {
            List<Rating> ratings = work.get().getRatingList();
            if (ratings.isEmpty()) {
                work.get().setRating(null);
            }
            BigDecimal newRating = BigDecimal.valueOf(
                    ratings
                            .stream()
                            .mapToDouble(Rating::getRate)
                            .average()
                            .getAsDouble()
            );
            work.get().setRating(newRating);
            workRepository.save(work.get());
        }
    }
}
