package com.github.PublishInn.controller;

import com.github.PublishInn.dto.ResetPasswordDto;
import com.github.PublishInn.dto.UserEmailDto;
import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AppUserService userService;

    @GetMapping("/info")
    public UserInfoDto getOwnAccountInfo(Principal principal) {
        return userService.getUserAccountInfo(principal.getName());
    }

    @PostMapping("/password/reset")
    public void sendResetPasswordCode(@RequestBody UserEmailDto model) throws UserException {
        userService.sendResetPasswordCode(model.getEmail());
    }

    @PostMapping("/password/reset/confirm")
    public void resetPassword(@RequestBody ResetPasswordDto model) {
        userService.resetPassword(model);
    }
}
