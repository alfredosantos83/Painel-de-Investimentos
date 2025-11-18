package com.caixa.invest.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimulacaoRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidSimulacaoRequest() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testClienteIdNotNull() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(null)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("clienteId é obrigatório"));
    }

    @Test
    void testClienteIdPositive() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(0L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("clienteId deve ser positivo"));
    }

    @Test
    void testValorNotNull() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(null)
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("valor é obrigatório"));
    }

    @Test
    void testValorMinimum() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(BigDecimal.ZERO)
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("valor deve ser maior que zero"));
    }

    @Test
    void testPrazoMesesNotNull() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(null)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("prazoMeses é obrigatório"));
    }

    @Test
    void testPrazoMesesMinimum() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(0)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("prazoMeses deve ser no mínimo 1"));
    }

    @Test
    void testPrazoMesesMaximum() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(361)
                .tipoProduto("CDB")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("prazoMeses deve ser no máximo 360"));
    }

    @Test
    void testTipoProdutoNotBlank() {
        SimulacaoRequest request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .tipoProduto("")
                .build();

        Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("tipoProduto é obrigatório"));
    }

    @Test
    void testLombokGettersSetters() {
        SimulacaoRequest request = new SimulacaoRequest();
        request.setClienteId(1L);
        request.setValor(new BigDecimal("1000.00"));
        request.setPrazoMeses(12);
        request.setTipoProduto("CDB");

        assertEquals(1L, request.getClienteId());
        assertEquals(new BigDecimal("1000.00"), request.getValor());
        assertEquals(12, request.getPrazoMeses());
        assertEquals("CDB", request.getTipoProduto());
    }

    @Test
    void testLombokEqualsAndHashCode() {
        SimulacaoRequest request1 = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        SimulacaoRequest request2 = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
