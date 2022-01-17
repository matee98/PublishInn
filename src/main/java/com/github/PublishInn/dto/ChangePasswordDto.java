package com.github.PublishInn.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ChangePasswordDto {
    @NotBlank
    @Size(min = 8)
    private String oldPassword;
    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
