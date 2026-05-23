package com.example.locadora.service;

import com.example.locadora.entity.Usuario;

import java.time.Instant;
import java.util.*;

public class TokenService {

    private final Map<String, Session> sessions = new HashMap<>();

    public String emitToken(Usuario usuario) {

        String token = UUID.randomUUID().toString();

        Session session = new Session(
                usuario.getUsername(),
                usuario.getRole(),
                Instant.now().plusSeconds(3600)
        );

        sessions.put(token, session);

        return token;
    }

    public Optional<Session> validate(String token) {

        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        Session session = sessions.get(token);

        if (session == null) {
            return Optional.empty();
        }

        if (session.expiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }

        return Optional.of(session);
    }

    public void revoke(String token) {
        sessions.remove(token);
    }

    public record Session(String username, Object role, Instant expiresAt) {}
}