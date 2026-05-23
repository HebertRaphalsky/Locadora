package com.example.locadora.service;

import com.example.locadora.config.AppSecurityProperties;
import com.example.locadora.config.SecurityMode;
import com.example.locadora.dto.DevolucaoRequest;
import com.example.locadora.dto.LocacaoRequest;
import com.example.locadora.dto.LocacaoResponse;
import com.example.locadora.entity.Cliente;
import com.example.locadora.entity.Jogo;
import com.example.locadora.entity.Locacao;
import com.example.locadora.entity.LocacaoStatus;
import com.example.locadora.repository.LocacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocacaoServiceTest {

    private LocacaoRepository locacaoRepository;
    private ClienteService clienteService;
    private JogoService jogoService;
    private AppSecurityProperties securityProperties;

    private LocacaoService locacaoService;

    @BeforeEach
    void setup() {
        locacaoRepository = mock(LocacaoRepository.class);
        clienteService = mock(ClienteService.class);
        jogoService = mock(JogoService.class);

        securityProperties = new AppSecurityProperties();
        securityProperties.setMode(SecurityMode.SECURE);

        locacaoService = new LocacaoService(locacaoRepository, clienteService, jogoService, securityProperties);
    }

    @Test
    void deveRegistrarLocacao() {
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(1L);

        Jogo jogo = new Jogo();
        jogo.setId(2L);
        jogo.setTitulo("Jogo");
        jogo.setPrecoDiaria(new BigDecimal("10.00"));
        jogo.setDisponivel(true);

        when(clienteService.buscar(1L)).thenReturn(cliente);
        when(jogoService.buscarOuErro(2L)).thenReturn(jogo);

        when(locacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        LocacaoResponse resp = locacaoService.registrar(new LocacaoRequest(1L, 2L, 2));

        assertEquals(1L, resp.clienteId());
        assertEquals(2L, resp.jogoId());
        assertEquals(new BigDecimal("20.00"), resp.valorTotal());
        assertEquals("ABERTA", resp.status());
    }

    @Test
    void deveAplicarMultaQuandoAtrasarDevolucao() {
        Jogo jogo = new Jogo();
        jogo.setId(2L);
        jogo.setTitulo("Jogo");
        jogo.setPrecoDiaria(new BigDecimal("10.00"));
        jogo.setDisponivel(false);

        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(1L);

        Locacao locacao = new Locacao();
        locacao.setId(10L);
        locacao.setCliente(cliente);
        locacao.setJogo(jogo);
        locacao.setDataLocacao(LocalDate.now().minusDays(5));
        locacao.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));
        locacao.setValorTotal(new BigDecimal("20.00"));
        locacao.setStatus(LocacaoStatus.ABERTA);

        when(locacaoRepository.findById(10L)).thenReturn(Optional.of(locacao));
        when(locacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        LocalDate devolucaoAtrasada = LocalDate.now(); // 1 dia após prevista
        LocacaoResponse resp = locacaoService.registrarDevolucao(10L, new DevolucaoRequest(devolucaoAtrasada));

        assertEquals("FECHADA", resp.status());
        // multa: 10 * 1 * 0.5 = 5 => 20 + 5 = 25
        assertTrue(resp.valorTotal().compareTo(new BigDecimal("25.00")) == 0);
        assertTrue(jogo.isDisponivel());
    }
}