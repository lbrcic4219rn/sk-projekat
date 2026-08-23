package com.sk.hotelnotificationservice.security;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.sk.hotelnotificationservice.security.service.TokenService;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Configuration
public class SecurityAspect {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private final TokenService tokenService;

    @Around("@annotation(com.sk.hotelnotificationservice.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")
                    && joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                token = joinPoint.getArgs()[i].toString().split(" ")[1];
            }
        }
        if (token == null) {
            log.warn("Rejected request to {}: missing bearer token", method.getName());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Claims> parsedClaims = tokenService.parseToken(token);
        if (parsedClaims.isEmpty()) {
            log.warn("Rejected request to {}: invalid token", method.getName());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Claims claims = parsedClaims.get();
        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String role = claims.get("role", String.class);
        if (Arrays.asList(checkSecurity.roles()).contains(role)) {
            return joinPoint.proceed();
        }
        log.warn("Rejected request to {}: role {} not permitted", method.getName(), role);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}

