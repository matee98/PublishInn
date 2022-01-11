package com.github.PublishInn.exceptions;

public class RatingException extends Exception {
    private static final String RATING_ALREADY_EXISTS = "exception.rating.exists";
    private static final String CANNOT_RATE_OWN_WORK = "exception.rating.own_work.rate";

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
}
