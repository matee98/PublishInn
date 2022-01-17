package com.github.PublishInn.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ResetPasswordDto {
    @NotBlank
    @Size(min = 8)
    private String newPassword;
    private String code;
}
