package com.github.PublishInn.controller;

import com.github.PublishInn.dto.NewRatingDto;
import com.github.PublishInn.dto.RatingDetailsDto;
import com.github.PublishInn.exceptions.RatingException;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.exceptions.WorkException;
import com.github.PublishInn.service.RatingService;
import com.github.PublishInn.validation.Username;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ratings")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public RatingDetailsDto getRating(@RequestParam(value="username") @Valid @Username String username,
                                      @RequestParam(value="work_id") Long workId) {
        try {
            return ratingService.getRating(username, workId);
        } catch (UserException | RatingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public List<RatingDetailsDto> getRatingsByUsername(@PathVariable @Valid @Username String username) {
        try {
            return ratingService.getRatingsByUsername(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewRating(@RequestBody @Valid NewRatingDto model, Principal principal) {
        try {
            ratingService.addNewRating(model, principal);
        } catch (RatingException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (WorkException | UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping
    public void updateRating(@RequestBody @Valid NewRatingDto model, Principal principal) {
        try {
            ratingService.updateRating(model, principal);
        } catch (RatingException | UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
