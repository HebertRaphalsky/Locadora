package com.example.locadora.security;

import com.example.locadora.entity.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final Map<String, UsuarioSession> sessions = new ConcurrentHashMap<>();

    public String emitToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        Instant expires = Instant.now().plus(2, ChronoUnit.HOURS);

        sessions.put(token, new UsuarioSession(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                expires
        ));

        return token;
    }

    public Optional<UsuarioSession> validate(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        UsuarioSession session = sessions.get(token);
        if (session == null) {
            return Optional.empty();
        }

        if (session.expiresAt() != null && session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(token);
            return Optional.empty();
        }

        return Optional.of(session);
    }

    // (Opcional) Se quiser invalidar um token manualmente
    public void revoke(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}