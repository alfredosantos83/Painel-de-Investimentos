package com.caixa.invest.controller;

import com.caixa.invest.security.PasswordEncoder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DebugControllerUnitTest {

    @Inject
    DebugController debugController;

    @Inject
    PasswordEncoder passwordEncoder;

    @Test
    void testGetUsers() {
        // Act
        Map<String, Object> result = debugController.getUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("count"));
        assertTrue(result.containsKey("users"));
        assertEquals(2, result.get("count")); // admin e user do data.sql
    }

    @Test
    void testTestPasswordWithValidUser() {
        // Act
        Map<String, Object> result = debugController.testPassword("admin", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("admin", result.get("username"));
        assertTrue((Boolean) result.get("passwordMatches"));
        assertEquals(true, result.get("enabled"));
        assertFalse(result.containsKey("error"));
    }

    @Test
    void testTestPasswordWithInvalidPassword() {
        // Act
        Map<String, Object> result = debugController.testPassword("admin", "wrongpassword");

        // Assert
        assertNotNull(result);
        assertEquals("admin", result.get("username"));
        assertFalse((Boolean) result.get("passwordMatches"));
    }

    @Test
    void testTestPasswordWithNonExistentUser() {
        // Act
        Map<String, Object> result = debugController.testPassword("nonexistent", "password");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("error"));
        assertEquals("Usuário não encontrado", result.get("error"));
    }

    @Test
    void testGenerateHash() {
        // Arrange
        String password = "testpassword";

        // Act
        Map<String, String> result = debugController.generateHash(password);

        // Assert
        assertNotNull(result);
        assertEquals(password, result.get("password"));
        assertNotNull(result.get("hash"));
        assertTrue(result.get("hash").startsWith("$2a$"));
    }

    @Test
    void testGenerateHashWithEmptyPassword() {
        // Arrange
        String password = "";

        // Act
        Map<String, String> result = debugController.generateHash(password);

        // Assert
        assertNotNull(result);
        assertEquals("", result.get("password"));
        assertNotNull(result.get("hash"));
    }
}
