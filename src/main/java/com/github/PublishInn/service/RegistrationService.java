package com.github.PublishInn.service;

import com.github.PublishInn.dto.UserRegisterDto;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.AppUserRole;
import com.github.PublishInn.model.entity.token.ConfirmationToken;
import com.github.PublishInn.utils.EmailBuilder;
import com.github.PublishInn.utils.EmailSender;
import com.github.PublishInn.validation.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private static final String EMAIL_NOT_VALID = "Email not valid";
    private static final String TOKEN_NOT_FOUND_MSG = "Token not found";
    private static final String EMAIL_ALREADY_CONFIRMED_MSG = "Email has been already confirmed";
    private static final String TOKEN_EXPIRED_MSG = "Token has been expired already";

    private static final String CONFIRMATION_LINK = "http://localhost:8080/api/registration/confirm?token=";

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(UserRegisterDto model) {
        boolean isEmailValid = emailValidator.test(model.getEmail());
        if (!isEmailValid) {
            throw new IllegalStateException(EMAIL_NOT_VALID);
        }
        String token = appUserService.signUpUser(
                new AppUser(
                        model.getUsername(),
                        model.getEmail(),
                        model.getPassword(),
                        AppUserRole.USER
                )
        );
//        emailSender.send(
//                model.getEmail(),
//                EmailBuilder.buildEmail(
//                        model.getUsername(),
//                        CONFIRMATION_LINK + token),
//                "Email confirmation");

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException(TOKEN_NOT_FOUND_MSG));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(EMAIL_ALREADY_CONFIRMED_MSG);
        }

        if (LocalDateTime.now().isAfter(confirmationToken.getExpiresAt())) {
            throw new IllegalStateException(TOKEN_EXPIRED_MSG);
        }

        confirmationTokenService.setConfirmedAtToken(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
