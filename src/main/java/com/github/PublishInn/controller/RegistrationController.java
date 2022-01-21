package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserRegisterDto;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.service.RegistrationService;
import com.github.PublishInn.validation.OneTimeCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public void register(@RequestBody @Valid UserRegisterDto model) {
        try {
            registrationService.register(model);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/confirm")
    public void confirm(@RequestParam("token") @Valid @OneTimeCode String token) {
        registrationService.confirmToken(token);
    }
}
