package com.example.locadora.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioEntityTest {

    @Test
    void deveSetarEObterCampos() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setUsername("admin");
        u.setSenha("hash");
        u.setNome("Administrador");
        u.setRole(RoleType.ADMIN);
        u.setEmail("a@a.com");

        assertEquals(1L, u.getId());
        assertEquals("admin", u.getUsername());
        assertEquals("hash", u.getSenha());
        assertEquals("Administrador", u.getNome());
        assertEquals(RoleType.ADMIN, u.getRole());
        assertEquals("a@a.com", u.getEmail());
        assertNotNull(u.getCriadoEm());
    }
}