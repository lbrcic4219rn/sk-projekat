package com.sk.hoteluserservice.security.service.impl;

import com.sk.hoteluserservice.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    @Override
    public String generate(Claims claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(key())
                .compact();
    }

    @Override
    public Optional<Claims> parseToken(String jwt) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
