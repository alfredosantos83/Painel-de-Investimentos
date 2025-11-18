package com.caixa.invest.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testBuilder() {
        AuthResponse response = AuthResponse.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
                .type("Bearer")
                .username("admin")
                .role("ADMIN")
                .build();

        assertNotNull(response);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("admin", response.getUsername());
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    void testNoArgsConstructor() {
        AuthResponse response = new AuthResponse();
        assertNotNull(response);
        assertNull(response.getToken());
        assertNull(response.getType());
        assertNull(response.getUsername());
        assertNull(response.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        AuthResponse response = new AuthResponse(
                "token123",
                "Bearer",
                "user",
                "USER"
        );

        assertEquals("token123", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("user", response.getUsername());
        assertEquals("USER", response.getRole());
    }

    @Test
    void testGettersSetters() {
        AuthResponse response = new AuthResponse();
        response.setToken("newToken");
        response.setType("Bearer");
        response.setUsername("testuser");
        response.setRole("USER");

        assertEquals("newToken", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("testuser", response.getUsername());
        assertEquals("USER", response.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthResponse response1 = AuthResponse.builder()
                .token("token123")
                .type("Bearer")
                .username("admin")
                .role("ADMIN")
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .token("token123")
                .type("Bearer")
                .username("admin")
                .role("ADMIN")
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testNotEquals() {
        AuthResponse response1 = AuthResponse.builder()
                .token("token123")
                .type("Bearer")
                .username("admin")
                .role("ADMIN")
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .token("token456")
                .type("Bearer")
                .username("user")
                .role("USER")
                .build();

        assertNotEquals(response1, response2);
    }

    @Test
    void testToString() {
        AuthResponse response = AuthResponse.builder()
                .token("token123")
                .type("Bearer")
                .username("admin")
                .role("ADMIN")
                .build();

        String toString = response.toString();
        assertTrue(toString.contains("token123"));
        assertTrue(toString.contains("Bearer"));
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("ADMIN"));
    }
}
