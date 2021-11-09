package com.github.PublishInn.service;

import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.security.provider.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final JWTTokenProvider tokenProvider;

    public Map<String, String> refreshToken(String refreshToken){
        String token = tokenProvider.getAccessToken(refreshToken);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", token);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }
}
