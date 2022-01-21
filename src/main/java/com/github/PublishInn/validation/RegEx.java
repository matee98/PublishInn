package com.github.PublishInn.validation;

public class RegEx {
    public static final String PASSWORD = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,64}$";
    public static final String USERNAME = "^[a-zA-Z0-9]+$";

    public static final String CONFIRM_TOKEN = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$";
}
