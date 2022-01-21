package com.github.PublishInn.exceptions;

public class RatingException extends Exception {
    public static final String RATING_ALREADY_EXISTS = "exception.rating.exists";
    public static final String CANNOT_RATE_OWN_WORK = "exception.rating.own_work.rate";
    public static final String NOT_FOUND = "exception.rating.not_found";

    private RatingException(String message, Throwable cause) {
        super(message, cause);
    }

    private RatingException(String message) {
        super(message);
    }

    public static RatingException ratingAlreadyExists() {
        return new RatingException(RATING_ALREADY_EXISTS);
    }

    public static RatingException ownWorkRateTry() {
        return new RatingException(CANNOT_RATE_OWN_WORK);
    }

    public static RatingException notFound() {
        return new RatingException(NOT_FOUND);
    }
}
