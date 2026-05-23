package com.example.locadora.controller;

import com.example.locadora.dto.UsuarioRequest;
import com.example.locadora.dto.UsuarioResponse;
import com.example.locadora.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Test
    void deveCriarUsuarioChamandoService() {
        UsuarioService service = mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController(service);

        UsuarioRequest req = new UsuarioRequest("user1", "123456", "Nome", "ADMIN", "a@a.com");
        UsuarioResponse resp = new UsuarioResponse(1L, "user1", "Nome", "ADMIN", "a@a.com");

        when(service.criarUsuario(req)).thenReturn(resp);

        ResponseEntity<UsuarioResponse> out = controller.criar(req);

        assertEquals(201, out.getStatusCodeValue());
        assertEquals(1L, out.getBody().id());
        verify(service).criarUsuario(req);
    }
}