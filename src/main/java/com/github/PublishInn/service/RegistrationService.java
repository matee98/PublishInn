package com.github.PublishInn.service;

import com.github.PublishInn.dto.UserRegisterDto;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public String register(UserRegisterDto model) {
        return "works";
    }
}
