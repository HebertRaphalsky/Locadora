package com.example.locadora.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.locadora.config.AppSecurityProperties;
import com.example.locadora.config.SecurityMode;
import com.example.locadora.dto.UsuarioRequest;
import com.example.locadora.entity.Usuario;
import com.example.locadora.repository.UsuarioRepository;
import com.example.locadora.util.InputSanitizer;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private InputSanitizer inputSanitizer;
    private AppSecurityProperties securityProperties;

    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        inputSanitizer = mock(InputSanitizer.class);

        securityProperties = new AppSecurityProperties();
        // força o modo seguro (assim o teste valida o comportamento correto)
        securityProperties.setMode(SecurityMode.SECURE);

        usuarioService = new UsuarioService(
                usuarioRepository,
                passwordEncoder,
                inputSanitizer,
                securityProperties
        );
    }

    @Test
    void deveSalvarSenhaCriptografada() {
        // Arrange
        UsuarioRequest request = new UsuarioRequest(
                "admin",
                "123456",
                "Administrador",
                "ADMIN",
                "admin@email.com"
        );

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.empty());

        // Sanitização no modo seguro (vamos devolver o mesmo texto para simplificar o teste)
        when(inputSanitizer.sanitize(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Encoder deve transformar a senha
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$HASH_BCRYPT_EXEMPLO");

        // Repository salva e devolve o mesmo objeto
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        usuarioService.criarUsuario(request);

        // Assert (captura o Usuario salvo para validar senha)
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario salvo = captor.getValue();

        assertNotNull(salvo.getSenha());
        assertNotEquals("123456", salvo.getSenha(), "A senha NÃO pode ser armazenada em texto plano");
        assertEquals("$2a$10$HASH_BCRYPT_EXEMPLO", salvo.getSenha(), "A senha deve ser armazenada como hash");

        // garante que encode foi chamado (ou seja, realmente criptografou)
        verify(passwordEncoder, times(1)).encode("123456");
    }
}