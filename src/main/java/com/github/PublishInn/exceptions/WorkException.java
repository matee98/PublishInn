package com.github.PublishInn.exceptions;

public class WorkException extends Exception{
    private static final String ACCESS_FORBIDDEN = "exception.work.access.forbidden";
    private static final String NOT_FOUND = "exception.work.not_found";

    private WorkException(String message, Throwable cause) {
        super(message, cause);
    }

    private WorkException(String message) {
        super(message);
    }

    public static WorkException accessForbidden() {
        return new WorkException(ACCESS_FORBIDDEN);
    }

    public static WorkException notFound() {
        return new WorkException(NOT_FOUND);
    }
}
