package com.github.PublishInn.service;

import com.github.PublishInn.dto.UserRegisterDto;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.token.ConfirmationToken;
import com.github.PublishInn.utils.EmailBuilder;
import com.github.PublishInn.utils.EmailSender;
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

    private static final String CONFIRMATION_LINK = "https://publish-inn.herokuapp.com/register/confirm/";

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public void register(UserRegisterDto model) throws UserException {
        String token = appUserService.signUpUser(
                new AppUser(
                        model.getUsername(),
                        model.getEmail(),
                        model.getPassword(),
                        AppUserRole.USER
                )
        );
        emailSender.send(
                model.getEmail(),
                EmailBuilder.buildRegisterEmail(
                        model.getUsername(),
                        CONFIRMATION_LINK + token),
                "Potwierdź adres e-mail");
    }

    @Transactional
    public void confirmToken(String token) {
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
    }
}
