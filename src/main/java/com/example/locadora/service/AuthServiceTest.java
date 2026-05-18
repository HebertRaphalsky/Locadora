package com.example.locadora.service;

import com.example.locadora.dto.LoginRequest;
import com.example.locadora.entity.RoleType;
import com.example.locadora.entity.Usuario;
import com.example.locadora.exception.BusinessException;
import com.example.locadora.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario user = new Usuario();
        user.setUsername("admin");
        user.setSenha(passwordEncoder.encode("123"));
        user.setNome("Administrador");
        user.setRole(RoleType.ADMIN);

        usuarioRepository.save(user);
    }

    @Test
    void deveFalharComSqlInjection() {

        LoginRequest request = new LoginRequest(
                "admin' OR '1'='1",
                "123"
        );

        assertThrows(BusinessException.class, () -> {
            authService.login(request);
        });
    }
}
