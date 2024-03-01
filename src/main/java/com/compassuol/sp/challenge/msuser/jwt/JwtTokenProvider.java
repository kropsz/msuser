package com.compassuol.sp.challenge.msuser.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.compassuol.sp.challenge.msuser.exception.TokenVerificationException;
import com.compassuol.sp.challenge.msuser.model.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenProvider {

    @Value("${api.security.token.secret}")
    private String secret;

    public String createToken(User user) {
        log.info("Iniciando a criação do token para o usuário {}", user.getEmail());
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-application")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            log.info("Token criado com sucesso para o usuário");
            return token;
        } catch (JWTCreationException ex) {
            throw new TokenVerificationException("Erro ao criar o token JWT");
        }
    }

    public String validateToken(String token) {
        log.info("Iniciando a validação do token");
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-application")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("Token validado com sucesso. Sujeito");
            return jwt.getSubject();
        } catch (JWTVerificationException ex) {
            log.warn("Lançada a RuntimeException");
            throw new TokenVerificationException("Erro ao validar o token JWT");
        }
    }

    private Instant genExpirationDate() {
        log.info("Gerando data de expiração do token");
        Instant expirationDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        log.info("Data de expiração do token gerada: {}", expirationDate);
        return expirationDate;
    }
}