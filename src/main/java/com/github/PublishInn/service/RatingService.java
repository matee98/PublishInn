package com.github.PublishInn.service;

import com.github.PublishInn.dto.NewRatingDto;
import com.github.PublishInn.dto.RatingDetailsDto;
import com.github.PublishInn.dto.mappers.RatingMapper;
import com.github.PublishInn.exceptions.RatingException;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.exceptions.WorkException;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;


    public RatingDetailsDto getRating(String username, Long workId) throws UserException, RatingException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw UserException.notFound();
        }
        RatingMapper mapper = Mappers.getMapper(RatingMapper.class);
        return ratingRepository.findRatingByUserIdAndWorkId(user.get().getId(), workId)
                .map(mapper::fromRating)
                .map(ratingDetailsDto -> {
                    Optional<Work> work = workRepository.findById(ratingDetailsDto.getWorkId());
                    if (work.isPresent()) {
                        ratingDetailsDto.setTitle(work.get().getTitle());
                        ratingDetailsDto.setAuthorName(userRepository.getById(work.get().getUserId()).getUsername());
                    }
                    ratingDetailsDto.setUsername(username);
                    return ratingDetailsDto;
                })
                .orElseThrow(RatingException::notFound);
    }

    public List<RatingDetailsDto> getRatingsByUsername(String username) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        RatingMapper mapper = Mappers.getMapper(RatingMapper.class);
        if (user.isPresent()) {
            return ratingRepository.findRatingsByUserId(user.get().getId())
                    .stream()
                    .map(rating -> {
                        RatingDetailsDto result = mapper.fromRating(rating);
                        Optional<Work> work = workRepository.findById(rating.getWorkId());
                        if (work.isPresent()) {
                            result.setTitle(work.get().getTitle());
                            result.setAuthorName(userRepository.getById(work.get().getUserId()).getUsername());
                        }
                        return result;
                    })
                    .peek(ratingDetailsDto -> ratingDetailsDto.setUsername(username))
                    .collect(Collectors.toList());
        } else {
            throw UserException.notFound();
        }
    }

    public void addNewRating(@Valid NewRatingDto model, Principal principal) throws RatingException, UserException, WorkException {
        RatingMapper mapper = Mappers.getMapper(RatingMapper.class);
        Rating rating = mapper.fromNewRatingDto(model);
        Optional<AppUser> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()){
            rating.setUserId(user.get().getId());
            rating.setCreatedBy(user.get().getId());
        } else {
            throw UserException.notFound();
        }
        Optional<Work> work = workRepository.findById(model.getWorkId());
        if (work.isPresent()) {
            rating.setWorkId(work.get().getId());
        } else {
            throw WorkException.notFound();
        }
        if(ratingRepository.findRatingByUserIdAndWorkId(user.get().getId(), work.get().getId()).isPresent()) {
            throw RatingException.ratingAlreadyExists();
        } else if(Objects.equals(work.get().getUserId(), user.get().getId())) {
            throw RatingException.ownWorkRateTry();
        }
        ratingRepository.save(rating);
        calculateWorkRating(rating.getWorkId());
    }

    public void updateRating(NewRatingDto model, Principal principal) throws UserException, RatingException {
        Optional<AppUser> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw UserException.notFound();
        }
        Optional<Rating> rating = ratingRepository.findRatingByUserIdAndWorkId(user.get().getId(), model.getWorkId());
        if (rating.isPresent()) {
            rating.get().setRate(model.getRate());
            rating.get().setModifiedBy(user.get().getId());
            ratingRepository.save(rating.get());
            calculateWorkRating(rating.get().getWorkId());
        } else {
            throw RatingException.notFound();
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
