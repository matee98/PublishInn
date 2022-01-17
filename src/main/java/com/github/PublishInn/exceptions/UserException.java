package com.github.PublishInn.exceptions;

public class UserException extends Exception{

    private static final String USER_NOT_FOUND = "User not found.";
    private static final String USERNAME_ALREADY_EXISTS = "Username already exists.";
    private static final String EMAIL_ALREADY_EXISTS = "Email already exists.";
    private static final String PASSWORD_NOT_MATCH = "Old password does not match.";

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
