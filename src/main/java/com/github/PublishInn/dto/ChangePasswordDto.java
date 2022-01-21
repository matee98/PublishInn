package com.github.PublishInn.dto;

import com.github.PublishInn.validation.Password;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ChangePasswordDto {
    @NotBlank
    @Password
    private String oldPassword;
    @NotBlank
    @Password
    private String newPassword;
}
