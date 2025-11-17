package com.caixa.invest.security;

import com.caixa.invest.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderUnitTest {

    @InjectMocks
    JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Simula a configuração do Quarkus
        try {
            var field = JwtTokenProvider.class.getDeclaredField("expiration");
            field.setAccessible(true);
            field.set(tokenProvider, 3600000L); // 1 hora
        } catch (Exception e) {
            fail("Erro ao configurar expiration: " + e.getMessage());
        }
    }

    @Test
    void testGenerateTokenWithUser() {
        // Arrange
        User user = new User();
        user.id = 1L;
        user.setUsername("testuser");
        user.setEmail("testuser@caixa.com");
        user.setRole(User.Role.USER);

        // Act
        String token = tokenProvider.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tem 3 partes separadas por ponto
    }

    @Test
    void testGenerateTokenWithAdminUser() {
        // Arrange
        User admin = new User();
        admin.id = 2L;
        admin.setUsername("admin");
        admin.setEmail("admin@caixa.com");
        admin.setRole(User.Role.ADMIN);

        // Act
        String token = tokenProvider.generateToken(admin);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void testGenerateTokenWithUsernameAndRoles() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");

        // Act
        String token = tokenProvider.generateToken("testuser", roles);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void testGenerateTokenWithSingleRole() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        // Act
        String token = tokenProvider.generateToken("singleuser", roles);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateTokenWithEmptyRoles() {
        // Arrange
        Set<String> roles = new HashSet<>();

        // Act
        String token = tokenProvider.generateToken("noroleuser", roles);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateTokensAreDifferent() {
        // Arrange
        User user1 = new User();
        user1.id = 1L;
        user1.setUsername("user1");
        user1.setEmail("user1@caixa.com");
        user1.setRole(User.Role.USER);

        User user2 = new User();
        user2.id = 2L;
        user2.setUsername("user2");
        user2.setEmail("user2@caixa.com");
        user2.setRole(User.Role.ADMIN);

        // Act
        String token1 = tokenProvider.generateToken(user1);
        String token2 = tokenProvider.generateToken(user2);

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }
}
