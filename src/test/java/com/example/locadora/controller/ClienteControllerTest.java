package com.example.locadora.controller;

import com.example.locadora.dto.ClienteRequest;
import com.example.locadora.dto.ClienteResponse;
import com.example.locadora.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Test
    void deveListarClientes() {
        ClienteService service = mock(ClienteService.class);
        ClienteController controller = new ClienteController(service);

        when(service.listar()).thenReturn(List.of(
                new ClienteResponse(1L, "C1", "c1@a.com", "123", "999")
        ));

        ResponseEntity<List<ClienteResponse>> out = controller.listar();

        assertEquals(200, out.getStatusCodeValue());
        assertEquals(1, out.getBody().size());
        verify(service).listar();
    }

    @Test
    void deveCriarCliente() {
        ClienteService service = mock(ClienteService.class);
        ClienteController controller = new ClienteController(service);

        ClienteRequest req = new ClienteRequest("C1", "c1@a.com", "12345678901", "999");
        ClienteResponse resp = new ClienteResponse(1L, "C1", "c1@a.com", "12345678901", "999");

        when(service.criar(req)).thenReturn(resp);

        ResponseEntity<ClienteResponse> out = controller.criar(req);

        assertEquals(201, out.getStatusCodeValue());
        assertEquals(1L, out.getBody().id());
        verify(service).criar(req);
    }
}