package com.github.PublishInn.service;

import com.github.PublishInn.dto.UserRegisterDto;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.AppUserRole;
import com.github.PublishInn.validation.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private static final String EMAIL_NOT_VALID = "Email not valid";

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public String register(UserRegisterDto model) {
        boolean isEmailValid = emailValidator.test(model.getEmail());
        if (!isEmailValid) {
            throw new IllegalStateException(EMAIL_NOT_VALID);
        }
        return appUserService.signUpUser(
                new AppUser(
                        model.getUsername(),
                        model.getEmail(),
                        model.getPassword(),
                        AppUserRole.USER
                )
        );
    }
}
