package com.github.PublishInn.controller;

import com.github.PublishInn.dto.ChangePasswordDto;
import com.github.PublishInn.dto.ResetPasswordDto;
import com.github.PublishInn.dto.UserEmailDto;
import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.security.Principal;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AppUserService userService;

    @GetMapping("/info")
    public UserInfoDto getOwnAccountInfo(Principal principal) {
        try {
            return userService.getUserAccountInfo(principal.getName());
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/password/reset")
    public void sendResetPasswordCode(@RequestBody UserEmailDto model) {
        try {
            userService.sendResetPasswordCode(model.getEmail());
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/password/reset/confirm")
    public void resetPassword(@RequestBody @Valid ResetPasswordDto model) {
        userService.resetPassword(model);
    }

    @PutMapping("/password/change")
    public void selfChangePassword(Principal principal, @Valid @RequestBody ChangePasswordDto model) throws UserException {
        userService.selfChangePassword(model, principal);
    }

    @PutMapping("/email/change")
    public void selfChangeEmail(@Valid @RequestBody UserEmailDto model, Principal principal) {
        try {
            userService.selfChangeEmail(model, principal);
        } catch (UserException e) {
            if (Objects.equals(e.getMessage(), UserException.EMAIL_ALREADY_EXISTS)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }
    }
}
