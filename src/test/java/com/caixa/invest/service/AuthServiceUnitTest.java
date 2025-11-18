package com.caixa.invest.service;

import com.caixa.invest.domain.User;
import com.caixa.invest.security.PasswordEncoder;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class AuthServiceUnitTest {

    @InjectMock
    PasswordEncoder passwordEncoder;

    @Inject
    AuthService authService;

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        when(passwordEncoder.matches("password123", "$2a$12$k7ebzNvmKCDiKFwxZX0yhueJOtxxfjOL8/Q6rw1rcwobieWCc3Y7S"))
                .thenReturn(true);

        // Act
        User user = authService.authenticate("admin", "password123");

        // Assert
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertTrue(user.getEnabled());
        verify(passwordEncoder, times(1)).matches("password123", user.getPassword());
    }

    @Test
    void testAuthenticateUserNotFound() {
        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            authService.authenticate("nonexistent", "password123");
        });

        assertEquals("Usuário ou senha inválidos", exception.getMessage());
    }

    @Test
    void testAuthenticateWrongPassword() {
        // Arrange
        when(passwordEncoder.matches("wrongpassword", "$2a$12$k7ebzNvmKCDiKFwxZX0yhueJOtxxfjOL8/Q6rw1rcwobieWCc3Y7S"))
                .thenReturn(false);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            authService.authenticate("admin", "wrongpassword");
        });

        assertEquals("Usuário ou senha inválidos", exception.getMessage());
    }

    @Test
    @Transactional
    void testAuthenticateDisabledUser() {
        // Create disabled user in database
        User disabledUser = new User();
        disabledUser.setUsername("disabled_user");
        disabledUser.setEmail("disabled@test.com");
        disabledUser.setPassword("$2a$12$k7ebzNvmKCDiKFwxZX0yhueJOtxxfjOL8/Q6rw1rcwobieWCc3Y7S");
        disabledUser.setRole(User.Role.USER);
        disabledUser.setEnabled(false);
        disabledUser.persist();

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            authService.authenticate("disabled_user", "password123");
        });

        assertEquals("Usuário desabilitado", exception.getMessage());
    }

    @Test
    void testFindByUsername() {
        // Act
        User user = authService.findByUsername("admin");

        // Assert
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("admin@caixa.com", user.getEmail());
    }

    @Test
    void testFindByUsernameNotFound() {
        // Act
        User user = authService.findByUsername("nonexistent");

        // Assert
        assertNull(user);
    }
}
