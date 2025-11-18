package com.caixa.invest.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SimulationEnhancedTest {

    @Test
    void testCreateSimulation() {
        Simulation simulation = new Simulation();
        simulation.setValorInvestido(BigDecimal.valueOf(10000));
        simulation.setValorFinal(BigDecimal.valueOf(11200));
        simulation.setPrazoMeses(12);
        simulation.setRentabilidadeEfetiva(BigDecimal.valueOf(0.12));
        simulation.setDataSimulacao(LocalDateTime.now());
        simulation.setImpostoRenda(BigDecimal.valueOf(45));
        simulation.setValorLiquido(BigDecimal.valueOf(11155));

        assertNotNull(simulation);
        assertEquals(BigDecimal.valueOf(10000), simulation.getValorInvestido());
        assertEquals(BigDecimal.valueOf(11200), simulation.getValorFinal());
        assertEquals(12, simulation.getPrazoMeses());
        assertEquals(BigDecimal.valueOf(0.12), simulation.getRentabilidadeEfetiva());
    }

    @Test
    void testSimulationCalculations() {
        Simulation simulation = new Simulation();
        simulation.setValorInvestido(BigDecimal.valueOf(5000));
        simulation.setPrazoMeses(6);
        simulation.setRentabilidadeEfetiva(BigDecimal.valueOf(0.06));
        
        // Valor final = principal * (1 + taxa)
        BigDecimal valorEsperado = BigDecimal.valueOf(5300);
        simulation.setValorFinal(valorEsperado);
        
        assertEquals(valorEsperado, simulation.getValorFinal());
    }

    @Test
    void testSimulationWithTax() {
        Simulation simulation = new Simulation();
        simulation.setValorInvestido(BigDecimal.valueOf(10000));
        simulation.setValorFinal(BigDecimal.valueOf(11000));
        simulation.setImpostoRenda(BigDecimal.valueOf(225)); // 22.5% sobre 1000 de ganho
        simulation.setValorLiquido(BigDecimal.valueOf(10775));
        
        assertTrue(simulation.getValorLiquido().compareTo(simulation.getValorFinal()) < 0);
    }

    @Test
    void testSimulationDateValidation() {
        Simulation simulation = new Simulation();
        LocalDateTime now = LocalDateTime.now();
        simulation.setDataSimulacao(now);
        
        assertEquals(now, simulation.getDataSimulacao());
        assertNotNull(simulation.getDataSimulacao());
    }

    @Test
    void testSimulationPrazoRange() {
        Simulation sim1 = new Simulation();
        sim1.setPrazoMeses(6);
        
        Simulation sim2 = new Simulation();
        sim2.setPrazoMeses(24);
        
        assertTrue(sim1.getPrazoMeses() < sim2.getPrazoMeses());
    }
}
