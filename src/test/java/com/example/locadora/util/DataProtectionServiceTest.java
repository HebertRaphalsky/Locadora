package com.example.locadora.service;

import com.example.locadora.config.AppSecurityProperties;
import com.example.locadora.util.DataProtectionService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataProtectionServiceTest {

    @Test
    void deveCriptografarEDescriptografar() {

        AppSecurityProperties props = new AppSecurityProperties();
        props.setEncryptionKey("chave-forte-teste-123456789");

        DataProtectionService service = new DataProtectionService(props);

        String original = "dado sensível";

        String protegido = service.protect(original);

        assertNotEquals(original, protegido);

        String revelado = service.reveal(protegido);

        assertEquals(original, revelado);
    }

    @Test
    void deveGerarResultadosDiferentesParaMesmoTexto() {

        AppSecurityProperties props = new AppSecurityProperties();
        props.setEncryptionKey("chave-forte-teste-123456789");

        DataProtectionService service = new DataProtectionService(props);

        String texto = "teste";

        String a = service.protect(texto);
        String b = service.protect(texto);

        assertNotEquals(a, b); 
    }
}