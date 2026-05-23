package com.example.locadora.util;

import com.example.locadora.entity.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarFalseSemAutenticacao() {
        assertFalse(SecurityUtil.hasRole(RoleType.ADMIN));
        assertNull(SecurityUtil.getUsername());
    }

    @Test
    void deveDetectarRoleAdminEUsername() {
        var auth = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(SecurityUtil.hasRole(RoleType.ADMIN));
        assertEquals("admin", SecurityUtil.getUsername());
    }

    @Test
    void deveDetectarRoleAtendente() {
        var auth = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ATENDENTE"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(SecurityUtil.hasRole(RoleType.ATENDENTE));
        assertFalse(SecurityUtil.hasRole(RoleType.ADMIN));
    }
}