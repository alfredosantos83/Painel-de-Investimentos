package com.caixa.invest.controller;

import com.caixa.invest.domain.User;
import com.caixa.invest.dto.request.LoginRequest;
import com.caixa.invest.dto.response.AuthResponse;
import com.caixa.invest.security.JwtTokenProvider;
import com.caixa.invest.service.AuthService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class AuthControllerUnitTest {

    @InjectMock
    AuthService authService;

    @InjectMock
    JwtTokenProvider tokenProvider;

    @Inject
    AuthController authController;

    @Test
    void testLoginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@caixa.com");
        user.setRole(User.Role.ADMIN);

        when(authService.authenticate(request.getUsername(), request.getPassword())).thenReturn(user);
        when(tokenProvider.generateToken(any(User.class))).thenReturn("mock-jwt-token");

        // Act
        Response response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        AuthResponse authResponse = (AuthResponse) response.getEntity();
        assertEquals("mock-jwt-token", authResponse.getToken());
        assertEquals("Bearer", authResponse.getType());
        assertEquals("admin", authResponse.getUsername());
        assertEquals("ROLE_ADMIN", authResponse.getRole());

        verify(authService, times(1)).authenticate(request.getUsername(), request.getPassword());
        verify(tokenProvider, times(1)).generateToken(user);
    }

    @Test
    void testLoginFailureInvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        when(authService.authenticate(request.getUsername(), request.getPassword()))
                .thenThrow(new SecurityException("Credenciais inválidas"));

        // Act
        Response response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatus());
        assertNotNull(response.getEntity());
        AuthController.ErrorResponse errorResponse = (AuthController.ErrorResponse) response.getEntity();
        assertEquals("Credenciais inválidas", errorResponse.message);

        verify(authService, times(1)).authenticate(request.getUsername(), request.getPassword());
        verify(tokenProvider, never()).generateToken(any(User.class));
    }

    @Test
    void testLoginWithUserRole() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@caixa.com");
        user.setRole(User.Role.USER);

        when(authService.authenticate(request.getUsername(), request.getPassword())).thenReturn(user);
        when(tokenProvider.generateToken(any(User.class))).thenReturn("user-jwt-token");

        // Act
        Response response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatus());
        AuthResponse authResponse = (AuthResponse) response.getEntity();
        assertEquals("ROLE_USER", authResponse.getRole());
        assertEquals("user", authResponse.getUsername());
    }
}
