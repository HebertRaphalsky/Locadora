package com.example.locadora.service;

import com.example.locadora.dto.JogoRequest;
import com.example.locadora.exception.NotFoundException;
import com.example.locadora.repository.JogoRepository;
import com.example.locadora.util.InputSanitizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JogoServiceUpdateTest {

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

    @Test
    void naoDeveAtualizarQuandoIdNaoExistir() {

        when(jogoRepository.findById(1L)).thenReturn(Optional.empty());

        // ✅ ordem correta do record: titulo, genero, precoDiaria, descricao
        JogoRequest request = new JogoRequest(
                "Jogo Teste",
                "Ação",
                new BigDecimal("10.0"),
                "Descrição"
        );

        assertThrows(NotFoundException.class, () -> jogoService.atualizar(1L, request));
    }
}