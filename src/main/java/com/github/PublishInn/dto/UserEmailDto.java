package com.github.PublishInn.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class UserEmailDto {
    @Email
    private String email;
}
