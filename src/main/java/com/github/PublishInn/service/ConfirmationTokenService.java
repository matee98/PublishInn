package com.github.PublishInn.service;

import com.github.PublishInn.model.entity.token.ConfirmationToken;
import com.github.PublishInn.model.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAtToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        confirmationToken.ifPresent(tok -> {
            tok.setConfirmedAt(LocalDateTime.now());
            confirmationTokenRepository.save(tok);
        });
    }
}
