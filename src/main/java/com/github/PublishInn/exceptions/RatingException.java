package com.github.PublishInn.exceptions;

public class RatingException extends Exception {
    private static final String RATING_ALREADY_EXISTS = "exception.rating.exists";

    private RatingException(String message, Throwable cause) {
        super(message, cause);
    }

    private RatingException(String message) {
        super(message);
    }

    public static RatingException ratingAlreadyExists() {
        return new RatingException(RATING_ALREADY_EXISTS);
    }
}
