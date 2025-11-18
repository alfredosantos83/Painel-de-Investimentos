package com.caixa.invest.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginRequest() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsernameNotBlank() {
        LoginRequest request = LoginRequest.builder()
                .username("")
                .password("password123")
                .build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("username é obrigatório"));
    }

    @Test
    void testPasswordNotBlank() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("")
                .build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("password é obrigatório"));
    }

    @Test
    void testBothFieldsBlank() {
        LoginRequest request = LoginRequest.builder()
                .username("")
                .password("")
                .build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertEquals(2, violations.size());
    }

    @Test
    void testLombokGettersSetters() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        assertEquals("testuser", request.getUsername());
        assertEquals("testpass", request.getPassword());
    }

    @Test
    void testLombokEqualsAndHashCode() {
        LoginRequest request1 = LoginRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        LoginRequest request2 = LoginRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testLombokToString() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        String toString = request.toString();
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("password123"));
    }
}
