package com.example.locadora.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputSanitizerTest {

    private final InputSanitizer sanitizer = new InputSanitizer();

    @Test
    void deveRemoverScriptMalicioso() {

        String entrada = "<script>alert('XSS')</script>";

        String resultado = sanitizer.sanitize(entrada);

        // ✅ valida o que realmente importa
        assertFalse(resultado.contains("<script>"));
        assertFalse(resultado.contains("</script>"));

        // ✅ resultado não pode ser igual à entrada original
        assertNotEquals(entrada, resultado);
    }

    @Test
    void devePermitirTextoNormal() {

        String entrada = "Nome válido";

        String resultado = sanitizer.sanitize(entrada);

        assertEquals("Nome válido", resultado);
    }

    @Test
    void naoDeveQuebrarComValorNulo() {

        String resultado = sanitizer.sanitize(null);

        assertNull(resultado);
    }
}