package com.example.locadora.service;

import com.example.locadora.config.AppSecurityProperties;
import com.example.locadora.dto.LoginRequest;
import com.example.locadora.dto.LoginResponse;
import com.example.locadora.entity.RoleType;
import com.example.locadora.entity.Usuario;
import com.example.locadora.exception.BusinessException;
import com.example.locadora.repository.UsuarioRepository;
import com.example.locadora.security.TokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private AppSecurityProperties securityProperties;

    private AuthService authService;

    @BeforeEach
    void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenService = mock(TokenService.class);
        securityProperties = new AppSecurityProperties();

        authService = new AuthService(
                usuarioRepository,
                passwordEncoder,
                tokenService,
                securityProperties
        );
    }

    @Test
    void deveLogarComCredenciaisValidas() {
        Usuario user = new Usuario();
        user.setUsername("admin");
        user.setSenha("senhaHash");
        user.setRole(RoleType.ADMIN);

        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123", "senhaHash"))
                .thenReturn(true);

        when(tokenService.emitToken(user))
                .thenReturn("token123");

        LoginRequest request = new LoginRequest("admin", "123");

        LoginResponse response = authService.login(request);

        assertEquals("token123", response.token());
        assertEquals("ADMIN", response.role());
    }

    @Test
    void deveFalharQuandoSenhaIncorreta() {
        Usuario user = new Usuario();
        user.setUsername("admin");
        user.setSenha("senhaHash");

        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("errada", "senhaHash"))
                .thenReturn(false);

        LoginRequest request = new LoginRequest("admin", "errada");

        assertThrows(BusinessException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    void deveFalharComSqlInjection() {
        LoginRequest request = new LoginRequest(
                "admin' OR '1'='1",
                "123"
        );

        when(usuarioRepository
                .findByUsername("admin' OR '1'='1"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            authService.login(request);
        });
    }
}