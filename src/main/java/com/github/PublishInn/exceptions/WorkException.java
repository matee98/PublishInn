package com.github.PublishInn.exceptions;

public class WorkException extends Exception{
    private static final String ACCESS_FORBIDDEN = "exception.work.access.forbidden";

    private WorkException(String message, Throwable cause) {
        super(message, cause);
    }

    private WorkException(String message) {
        super(message);
    }

    public static WorkException accessForbidden() {
        return new WorkException(ACCESS_FORBIDDEN);
    }
}
