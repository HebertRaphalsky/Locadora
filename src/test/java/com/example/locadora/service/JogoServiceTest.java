package com.example.locadora.service;

import com.example.locadora.entity.Jogo;
import com.example.locadora.entity.RoleType;
import com.example.locadora.exception.AccessDeniedBusinessException;
import com.example.locadora.repository.JogoRepository;
import com.example.locadora.util.InputSanitizer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JogoServiceTest {

    private JogoRepository jogoRepository;
    private InputSanitizer inputSanitizer;

    private JogoService jogoService;

    @BeforeEach
    void setup() {
        jogoRepository = mock(JogoRepository.class);
        inputSanitizer = mock(InputSanitizer.class);

        // ✅ agora o construtor tem apenas 2 parâmetros
        jogoService = new JogoService(jogoRepository, inputSanitizer);
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    private void autenticarComo(RoleType role) {
        var auth = new UsernamePasswordAuthenticationToken(
                "usuarioTeste",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void naoDevePermitirRemoverQuandoNaoForAdmin() {
        autenticarComo(RoleType.ATENDENTE);

        assertThrows(AccessDeniedBusinessException.class, () -> jogoService.remover(1L));

        verify(jogoRepository, never()).findById(anyLong());
        verify(jogoRepository, never()).delete(any());
    }

    @Test
    void devePermitirRemoverQuandoForAdmin() {
        autenticarComo(RoleType.ADMIN);

        when(jogoRepository.findById(1L)).thenReturn(Optional.of(new Jogo()));

        jogoService.remover(1L);

        verify(jogoRepository, times(1)).findById(1L);
        verify(jogoRepository, times(1)).delete(any(Jogo.class));
    }
}