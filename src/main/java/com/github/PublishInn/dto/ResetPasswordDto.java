package com.github.PublishInn.dto;

import com.github.PublishInn.validation.OneTimeCode;
import com.github.PublishInn.validation.Password;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ResetPasswordDto {
    @Password
    private String newPassword;
    @OneTimeCode
    private String code;
}
