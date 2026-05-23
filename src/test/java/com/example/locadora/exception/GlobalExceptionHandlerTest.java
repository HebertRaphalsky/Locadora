package com.example.locadora.exception;

import com.example.locadora.config.AppSecurityProperties;
import com.example.locadora.config.SecurityMode;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void deveRetornar404ParaNotFound() {
        AppSecurityProperties props = new AppSecurityProperties();
        props.setMode(SecurityMode.SECURE);

        GlobalExceptionHandler handler = new GlobalExceptionHandler(props);

        ResponseEntity<Map<String, Object>> resp = handler.handleNotFound(new NotFoundException("x"));

        assertEquals(404, resp.getStatusCodeValue());
        assertTrue(resp.getBody().containsKey("timestamp"));
        assertEquals("x", resp.getBody().get("error"));
    }

    @Test
    void deveRetornar400ParaBusiness() {
        AppSecurityProperties props = new AppSecurityProperties();
        props.setMode(SecurityMode.SECURE);

        GlobalExceptionHandler handler = new GlobalExceptionHandler(props);

        ResponseEntity<Map<String, Object>> resp = handler.handleBusiness(new BusinessException("b"));

        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("b", resp.getBody().get("error"));
    }

    @Test
    void emModoInseguroDeveIncluirStackTrace() {
        AppSecurityProperties props = new AppSecurityProperties();
        props.setMode(SecurityMode.INSECURE);

        GlobalExceptionHandler handler = new GlobalExceptionHandler(props);

        ResponseEntity<Map<String, Object>> resp = handler.handleGeneric(new RuntimeException("boom"));

        assertEquals(500, resp.getStatusCodeValue());
        assertTrue(resp.getBody().containsKey("stackTrace"));
    }
}