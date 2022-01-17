package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserRegisterDto;
import com.github.PublishInn.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public void register(@RequestBody UserRegisterDto model) {
        registrationService.register(model);
    }
    //todo: return information about existing username or email

    @GetMapping("/confirm")
    public void confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
    }
}
