package com.example.locadora.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppSecurityPropertiesTest {

    @Test
    void deveAlternarModoSeguroEInseguro() {
        AppSecurityProperties props = new AppSecurityProperties();

        props.setMode(SecurityMode.SECURE);
        assertTrue(props.isSecureMode());
        assertFalse(props.isInsecureMode());

        props.setMode(SecurityMode.INSECURE);
        assertFalse(props.isSecureMode());
        assertTrue(props.isInsecureMode());
    }

    @Test
    void deveSetarEncryptionKey() {
        AppSecurityProperties props = new AppSecurityProperties();
        props.setEncryptionKey("chave-teste-forte-123");
        assertEquals("chave-teste-forte-123", props.getEncryptionKey());
    }
}
