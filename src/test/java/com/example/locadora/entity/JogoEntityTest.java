package com.example.locadora.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class JogoEntityTest {

    @Test
    void deveSetarEObterCampos() {
        Jogo j = new Jogo();
        j.setId(10L);
        j.setTitulo("Titulo");
        j.setGenero("Ação");
        j.setPrecoDiaria(new BigDecimal("10.00"));
        j.setDisponivel(false);
        j.setDescricao("Desc");

        assertEquals(10L, j.getId());
        assertEquals("Titulo", j.getTitulo());
        assertEquals("Ação", j.getGenero());
        assertEquals(new BigDecimal("10.00"), j.getPrecoDiaria());
        assertFalse(j.isDisponivel());
        assertEquals("Desc", j.getDescricao());
    }
}