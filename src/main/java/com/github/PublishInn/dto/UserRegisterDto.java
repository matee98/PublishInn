package com.github.PublishInn.dto;

import com.github.PublishInn.validation.Password;
import com.github.PublishInn.validation.UserEmail;
import com.github.PublishInn.validation.Username;
import lombok.*;

@Getter
public class UserRegisterDto {
    @Username
    private String username;
    @UserEmail
    private String email;
    @Password
    private String password;

}
