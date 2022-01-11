package com.github.PublishInn.controller;

import com.github.PublishInn.dto.NewRatingDto;
import com.github.PublishInn.dto.RatingDetailsDto;
import com.github.PublishInn.exceptions.RatingException;
import com.github.PublishInn.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ratings")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public RatingDetailsDto getRating(@RequestParam(value="username") String username,
                                      @RequestParam(value="work_id") Long workId) {
        return ratingService.getRating(username, workId);
    }

    @GetMapping("/{username}")
    public List<RatingDetailsDto> getRatingsByUsername(@PathVariable String username) {
        try {
            return ratingService.getRatingsByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewRating(@RequestBody NewRatingDto model, Principal principal) {
        try {
            ratingService.addNewRating(model, principal);
        } catch (RatingException e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping
    public void updateRating(@RequestBody NewRatingDto model, Principal principal) {
        ratingService.updateRating(model, principal);
    }


}
