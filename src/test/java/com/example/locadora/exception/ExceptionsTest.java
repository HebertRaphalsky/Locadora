package com.example.locadora.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    void deveCriarBusinessException() {
        BusinessException ex = new BusinessException("erro");
        assertEquals("erro", ex.getMessage());
    }

    @Test
    void deveCriarNotFoundException() {
        NotFoundException ex = new NotFoundException("nao achou");
        assertEquals("nao achou", ex.getMessage());
    }

    @Test
    void deveCriarAccessDeniedBusinessException() {
        AccessDeniedBusinessException ex = new AccessDeniedBusinessException("negado");
        assertEquals("negado", ex.getMessage());
    }
}