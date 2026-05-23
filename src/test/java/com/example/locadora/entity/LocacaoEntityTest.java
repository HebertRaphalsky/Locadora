package com.example.locadora.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LocacaoEntityTest {

    @Test
    void deveSetarEObterCampos() {
        Locacao l = new Locacao();

        Cliente cliente = mock(Cliente.class);
        Jogo jogo = mock(Jogo.class);

        l.setId(1L);
        l.setCliente(cliente);
        l.setJogo(jogo);
        l.setDataLocacao(LocalDate.now());
        l.setDataDevolucaoPrevista(LocalDate.now().plusDays(2));
        l.setDataDevolucaoReal(LocalDate.now().plusDays(3));
        l.setValorTotal(new BigDecimal("50.00"));
        l.setStatus(LocacaoStatus.FECHADA);

        assertEquals(1L, l.getId());
        assertEquals(cliente, l.getCliente());
        assertEquals(jogo, l.getJogo());
        assertNotNull(l.getDataLocacao());
        assertNotNull(l.getDataDevolucaoPrevista());
        assertNotNull(l.getDataDevolucaoReal());
        assertEquals(new BigDecimal("50.00"), l.getValorTotal());
        assertEquals(LocacaoStatus.FECHADA, l.getStatus());
    }
}