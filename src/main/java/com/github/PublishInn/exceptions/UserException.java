package com.github.PublishInn.exceptions;

public class UserException extends Exception{

    public static final String USER_NOT_FOUND = "exception.user.not_found";
    public static final String PASSWORD_NOT_MATCH = "exception.user.password.not_match";
    public static final String EMAIL_ALREADY_EXISTS = "exception.user.email.exists";
    public static final String USERNAME_ALREADY_EXISTS = "exception.user.username.exists";

    public UserException (String message) {
        super(message);
    }

    public static UserException notFound() {
        return new UserException(USER_NOT_FOUND);
    }

    public static UserException usernameExists() {
        return new UserException(USERNAME_ALREADY_EXISTS);
    }

    public static UserException emailExists() {
        return new UserException(EMAIL_ALREADY_EXISTS);
    }

    public static UserException passwordNotMatch() {
        return new UserException(PASSWORD_NOT_MATCH);
    }
}
