package com.example.locadora.service;

import com.example.locadora.config.AppSecurityProperties;
import com.example.locadora.config.SecurityMode;
import com.example.locadora.dto.ClienteRequest;
import com.example.locadora.entity.Cliente;
import com.example.locadora.repository.ClienteRepository;
import com.example.locadora.util.DataProtectionService;
import com.example.locadora.util.InputSanitizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private ClienteRepository clienteRepository;
    private DataProtectionService dataProtectionService;
    private InputSanitizer inputSanitizer;
    private AppSecurityProperties securityProperties;

    private ClienteService clienteService;

    @BeforeEach
    void setup() {
        clienteRepository = mock(ClienteRepository.class);
        dataProtectionService = mock(DataProtectionService.class);
        inputSanitizer = mock(InputSanitizer.class);

        securityProperties = new AppSecurityProperties();
        securityProperties.setMode(SecurityMode.SECURE);

        clienteService = new ClienteService(clienteRepository, dataProtectionService, inputSanitizer, securityProperties);
    }

    @Test
    void deveCriarClienteComDocumentoProtegido() {
        ClienteRequest req = new ClienteRequest("Nome", "a@a.com", "12345678901", "999");

        when(clienteRepository.findAll()).thenReturn(List.of()); // sem duplicado
        when(inputSanitizer.sanitize(anyString())).thenAnswer(i -> i.getArgument(0));
        when(dataProtectionService.protect("12345678901")).thenReturn("ENC");
        when(clienteRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(dataProtectionService.reveal("ENC")).thenReturn("12345678901");

        clienteService.criar(req);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(captor.capture());

        Cliente salvo = captor.getValue();
        assertNotNull(salvo);
        // documento deve ter sido protegido
        verify(dataProtectionService).protect("12345678901");
    }
}
