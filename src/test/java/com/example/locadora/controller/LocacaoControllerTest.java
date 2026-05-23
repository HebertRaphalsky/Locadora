package com.example.locadora.controller;

import com.example.locadora.dto.DevolucaoRequest;
import com.example.locadora.dto.LocacaoRequest;
import com.example.locadora.dto.LocacaoResponse;
import com.example.locadora.service.LocacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocacaoControllerTest {

    @Test
    void deveRegistrarLocacao() {
        LocacaoService service = mock(LocacaoService.class);
        LocacaoController controller = new LocacaoController(service);

        LocacaoRequest req = new LocacaoRequest(1L, 2L, 3);
        LocacaoResponse resp = new LocacaoResponse(
                10L, 1L, 2L, "Jogo", LocalDate.now(),
                LocalDate.now().plusDays(3), null,
                new BigDecimal("30.00"), "ABERTA"
        );

        when(service.registrar(req)).thenReturn(resp);

        ResponseEntity<LocacaoResponse> out = controller.registrar(req);

        assertEquals(201, out.getStatusCodeValue());
        assertEquals(10L, out.getBody().id());
        verify(service).registrar(req);
    }

    @Test
    void deveListarLocacoes() {
        LocacaoService service = mock(LocacaoService.class);
        LocacaoController controller = new LocacaoController(service);

        when(service.listar()).thenReturn(List.of());

        ResponseEntity<List<LocacaoResponse>> out = controller.listar();

        assertEquals(200, out.getStatusCodeValue());
        verify(service).listar();
    }

    @Test
    void deveRegistrarDevolucao() {
        LocacaoService service = mock(LocacaoService.class);
        LocacaoController controller = new LocacaoController(service);

        DevolucaoRequest req = new DevolucaoRequest(LocalDate.now());
        LocacaoResponse resp = new LocacaoResponse(
                10L, 1L, 2L, "Jogo", LocalDate.now().minusDays(3),
                LocalDate.now(), LocalDate.now(),
                new BigDecimal("30.00"), "FECHADA"
        );

        when(service.registrarDevolucao(10L, req)).thenReturn(resp);

        ResponseEntity<LocacaoResponse> out = controller.devolver(10L, req);

        assertEquals(200, out.getStatusCodeValue());
        assertEquals("FECHADA", out.getBody().status());
        verify(service).registrarDevolucao(10L, req);
    }
}
