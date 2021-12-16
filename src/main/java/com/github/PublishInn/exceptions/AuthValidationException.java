package com.github.PublishInn.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthValidationException extends AuthenticationException {
    private static final String INVALID_CREDENTIALS = "exception.auth.credentials.invalid";

    private AuthValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    private AuthValidationException(String message) {
        super(message);
    }

    public static AuthValidationException invalidCredentials() {
        return new AuthValidationException(INVALID_CREDENTIALS);
    }
}
