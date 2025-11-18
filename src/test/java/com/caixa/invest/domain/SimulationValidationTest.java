package com.caixa.invest.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SimulationValidationTest {

    @Test
    void testPrePersistSetsDataSimulacao() {
        Simulation simulation = Simulation.builder()
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("11200"))
                .rentabilidadeEfetiva(new BigDecimal("0.12"))
                .prazoMeses(12)
                .build();

        assertNull(simulation.getDataSimulacao());

        simulation.prePersist();

        assertNotNull(simulation.getDataSimulacao());
    }

    @Test
    void testPrePersistDoesNotOverrideDataSimulacao() {
        LocalDateTime customDate = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        Simulation simulation = Simulation.builder()
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("11200"))
                .rentabilidadeEfetiva(new BigDecimal("0.12"))
                .prazoMeses(12)
                .dataSimulacao(customDate)
                .build();

        simulation.prePersist();

        assertEquals(customDate, simulation.getDataSimulacao());
    }

    @Test
    void testSimulationBuilder() {
        Simulation simulation = Simulation.builder()
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("12000"))
                .rentabilidadeEfetiva(new BigDecimal("0.20"))
                .prazoMeses(12)
                .impostoRenda(new BigDecimal("400"))
                .valorLiquido(new BigDecimal("11600"))
                .build();

        assertNotNull(simulation);
        assertEquals(new BigDecimal("10000"), simulation.getValorInvestido());
        assertEquals(new BigDecimal("12000"), simulation.getValorFinal());
        assertEquals(new BigDecimal("0.20"), simulation.getRentabilidadeEfetiva());
        assertEquals(12, simulation.getPrazoMeses());
        assertEquals(new BigDecimal("400"), simulation.getImpostoRenda());
        assertEquals(new BigDecimal("11600"), simulation.getValorLiquido());
    }

    @Test
    void testSimulationWithDifferentPrazos() {
        Simulation sim6Meses = Simulation.builder()
                .prazoMeses(6)
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("10600"))
                .rentabilidadeEfetiva(new BigDecimal("0.06"))
                .build();

        Simulation sim12Meses = Simulation.builder()
                .prazoMeses(12)
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("11200"))
                .rentabilidadeEfetiva(new BigDecimal("0.12"))
                .build();

        assertEquals(6, sim6Meses.getPrazoMeses());
        assertEquals(12, sim12Meses.getPrazoMeses());
        assertTrue(sim12Meses.getValorFinal().compareTo(sim6Meses.getValorFinal()) > 0);
    }

    @Test
    void testSimulationWithImpostoRenda() {
        Simulation simulation = Simulation.builder()
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("12000"))
                .rentabilidadeEfetiva(new BigDecimal("0.20"))
                .prazoMeses(12)
                .impostoRenda(new BigDecimal("400"))
                .valorLiquido(new BigDecimal("11600"))
                .build();

        assertNotNull(simulation.getImpostoRenda());
        assertNotNull(simulation.getValorLiquido());
        
        // Valor líquido = valor final - imposto renda
        BigDecimal expectedLiquido = simulation.getValorFinal().subtract(simulation.getImpostoRenda());
        assertEquals(expectedLiquido, simulation.getValorLiquido());
    }

    @Test
    void testPrePersistTimestamp() {
        Simulation simulation = Simulation.builder()
                .valorInvestido(new BigDecimal("10000"))
                .valorFinal(new BigDecimal("11200"))
                .rentabilidadeEfetiva(new BigDecimal("0.12"))
                .prazoMeses(12)
                .build();

        LocalDateTime before = LocalDateTime.now();
        simulation.prePersist();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(simulation.getDataSimulacao());
        assertTrue(!simulation.getDataSimulacao().isBefore(before));
        assertTrue(!simulation.getDataSimulacao().isAfter(after));
    }
}
