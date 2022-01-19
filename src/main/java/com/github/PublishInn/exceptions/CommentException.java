package com.github.PublishInn.exceptions;

public class CommentException extends Exception{
    public static final String ACCESS_FORBIDDEN = "exception.comment.access.forbidden";
    public static final String NOT_FOUND = "exception.comment.not_found";

    private CommentException(String message) {
        super(message);
    }

    public static CommentException accessForbidden() {
        return new CommentException(ACCESS_FORBIDDEN);
    }

    public static CommentException notFound() {
        return new CommentException(NOT_FOUND);
    }
}
