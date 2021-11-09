package com.github.PublishInn.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.PublishInn.model.entity.AppUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class JWTTokenProvider {
    private final String JWT_SECRET_KEY = "ajs^@*sdalASHA@!#!#@FHdska73#$kdf";
    private final long JWT_VALIDITY_TIME = 20;
    private final String JWT_ISSUER = "publishinn-app";
    private final Algorithm ALGORITHM = Algorithm.HMAC256(JWT_SECRET_KEY.getBytes());

    public String getAccessToken(AppUser user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_VALIDITY_TIME * 60 * 1000))
                .withIssuer(JWT_ISSUER)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(ALGORITHM);
    }

    public String getRefreshToken(AppUser user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_VALIDITY_TIME * 60 * 1000))
                .withIssuer(JWT_ISSUER)
                .sign(ALGORITHM);
    }

    public UsernamePasswordAuthenticationToken extractToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
