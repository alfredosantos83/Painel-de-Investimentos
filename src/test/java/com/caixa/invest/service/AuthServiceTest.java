package com.caixa.invest.service;

import com.caixa.invest.domain.User;
import com.caixa.invest.security.JwtTokenProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@QuarkusTest
class AuthServiceTest {

    @Inject
    AuthService authService;

    @InjectMock
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        Mockito.when(jwtTokenProvider.generateToken(any(User.class)))
                .thenReturn("mock-jwt-token");
        Mockito.when(jwtTokenProvider.generateToken(anyString(), anySet()))
                .thenReturn("mock-jwt-token");
    }

    @Test
    void testAuthenticateSuccess() {
        User user = authService.authenticate("admin", "password123");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals(User.Role.ADMIN, user.getRole());
    }

    @Test
    void testAuthenticateInvalidPassword() {
        assertThrows(SecurityException.class, () -> {
            authService.authenticate("admin", "wrongpassword");
        });
    }

    @Test
    void testAuthenticateUserNotFound() {
        assertThrows(SecurityException.class, () -> {
            authService.authenticate("nonexistent", "password");
        });
    }

    @Test
    void testAuthenticateNullUsername() {
        assertThrows(SecurityException.class, () -> {
            authService.authenticate(null, "password");
        });
    }

    @Test
    void testAuthenticateNullPassword() {
        // PasswordEncoder.matches lança NullPointerException quando password é null
        assertThrows(NullPointerException.class, () -> {
            authService.authenticate("admin", null);
        });
    }
}
