package com.example.locadora.service;

import com.example.locadora.entity.RoleType;
import com.example.locadora.entity.Usuario;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @Test
    void deveGerarTokenValidoComSessao() {

        TokenService tokenService = new TokenService();

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setRole(RoleType.ADMIN);

        String token = tokenService.emitToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());

        var sessionOpt = tokenService.validate(token);

        assertTrue(sessionOpt.isPresent());

        var session = sessionOpt.get();

        assertEquals("admin", session.username());
        assertEquals(RoleType.ADMIN, session.role());

        assertNotNull(session.expiresAt());
        assertTrue(session.expiresAt().isAfter(Instant.now()));
    }

    @Test
    void deveRejeitarTokenInvalido() {

        TokenService tokenService = new TokenService();

        assertTrue(tokenService.validate(null).isEmpty());
        assertTrue(tokenService.validate("").isEmpty());
        assertTrue(tokenService.validate("token-invalido").isEmpty());
    }

    @Test
    void deveInvalidarTokenRevogado() {

        TokenService tokenService = new TokenService();

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setUsername("teste");
        usuario.setRole(RoleType.ATENDENTE);

        String token = tokenService.emitToken(usuario);

        assertTrue(tokenService.validate(token).isPresent());

        tokenService.revoke(token);

        assertTrue(tokenService.validate(token).isEmpty());
    }

}