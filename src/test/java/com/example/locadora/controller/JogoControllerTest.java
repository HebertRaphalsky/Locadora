package com.example.locadora.controller;

import com.example.locadora.dto.JogoRequest;
import com.example.locadora.dto.JogoResponse;
import com.example.locadora.service.JogoService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JogoControllerTest {

    @Test
    void deveListarJogos() {
        JogoService service = mock(JogoService.class);
        JogoController controller = new JogoController(service);

        when(service.listar()).thenReturn(List.of(
                new JogoResponse(1L, "T", "G", new BigDecimal("10.0"), true, "D")
        ));

        ResponseEntity<List<JogoResponse>> out = controller.listar();

        assertEquals(200, out.getStatusCodeValue());
        assertEquals(1, out.getBody().size());
        verify(service).listar();
    }

    @Test
    void deveCriarJogo() {
        JogoService service = mock(JogoService.class);
        JogoController controller = new JogoController(service);

        JogoRequest req = new JogoRequest("T", "G", new BigDecimal("10.0"), "D"); // ordem correta [1](https://unimedcuiaba-my.sharepoint.com/personal/carlos_raphalsky_unimedcuiaba_coop_br/Documents/Arquivos%20de%20Chat%20do%20Microsoft%20Copilot/TokenService.java)
        JogoResponse resp = new JogoResponse(1L, "T", "G", new BigDecimal("10.0"), true, "D");

        when(service.criar(req)).thenReturn(resp);

        ResponseEntity<JogoResponse> out = controller.criar(req);

        assertEquals(201, out.getStatusCodeValue());
        assertEquals(1L, out.getBody().id());
        verify(service).criar(req);
    }

    @Test
    void deveAtualizarJogo() {
        JogoService service = mock(JogoService.class);
        JogoController controller = new JogoController(service);

        JogoRequest req = new JogoRequest("T", "G", new BigDecimal("10.0"), "D"); // ordem correta [1](https://unimedcuiaba-my.sharepoint.com/personal/carlos_raphalsky_unimedcuiaba_coop_br/Documents/Arquivos%20de%20Chat%20do%20Microsoft%20Copilot/TokenService.java)
        JogoResponse resp = new JogoResponse(1L, "T", "G", new BigDecimal("10.0"), true, "D");

        when(service.atualizar(1L, req)).thenReturn(resp);

        ResponseEntity<JogoResponse> out = controller.atualizar(1L, req);

        assertEquals(200, out.getStatusCodeValue());
        verify(service).atualizar(1L, req);
    }

    @Test
    void deveRemoverJogo() {
        JogoService service = mock(JogoService.class);
        JogoController controller = new JogoController(service);

        ResponseEntity<Void> out = controller.remover(1L);

        assertEquals(204, out.getStatusCodeValue());
        verify(service).remover(1L);
    }
}