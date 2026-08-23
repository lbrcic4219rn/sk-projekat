package com.sk.hotelnotificationservice.security.service;

import io.jsonwebtoken.Claims;

import java.util.Optional;

public interface TokenService {

    String generate(Claims claims);

    Optional<Claims> parseToken(String jwt);
}
