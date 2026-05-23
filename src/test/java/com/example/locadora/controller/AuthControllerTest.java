package com.example.locadora.controller;

import com.example.locadora.dto.LoginRequest;
import com.example.locadora.dto.LoginResponse;
import com.example.locadora.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Test
    void deveChamarLoginEServico() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        LoginRequest req = new LoginRequest("admin", "123");
        LoginResponse resp = new LoginResponse("token", "ADMIN", "SECURE");

        when(authService.login(req)).thenReturn(resp);

        ResponseEntity<LoginResponse> out = controller.login(req);

        assertEquals(200, out.getStatusCodeValue());
        assertEquals("token", out.getBody().token());
        verify(authService).login(req);
    }
}