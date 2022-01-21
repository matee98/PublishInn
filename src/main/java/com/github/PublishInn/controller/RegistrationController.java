package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserRegisterDto;
import com.github.PublishInn.service.RegistrationService;
import com.github.PublishInn.validation.OneTimeCode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public void register(@RequestBody @Valid UserRegisterDto model) {
        registrationService.register(model);
    }
    //todo: return information about existing username or email

    @GetMapping("/confirm")
    public void confirm(@RequestParam("token") @Valid @OneTimeCode String token) {
        registrationService.confirmToken(token);
    }
}
