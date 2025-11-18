package com.caixa.invest.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class InvestmentEnhancedTest {

    @Test
    void testCreateInvestment() {
        Investment investment = new Investment();
        investment.setTipo(Product.TipoProduto.CDB);
        investment.setValor(BigDecimal.valueOf(10000));
        investment.setRentabilidade(BigDecimal.valueOf(0.12));
        investment.setData(LocalDate.now());
        investment.setPrazoMeses(12);
        investment.setStatus(Investment.StatusInvestimento.ATIVO);

        assertNotNull(investment);
        assertEquals(Product.TipoProduto.CDB, investment.getTipo());
        assertEquals(BigDecimal.valueOf(10000), investment.getValor());
        assertEquals(BigDecimal.valueOf(0.12), investment.getRentabilidade());
        assertEquals(12, investment.getPrazoMeses());
        assertEquals(Investment.StatusInvestimento.ATIVO, investment.getStatus());
    }

    @Test
    void testInvestmentAllTypes() {
        for (Product.TipoProduto tipo : Product.TipoProduto.values()) {
            Investment investment = new Investment();
            investment.setTipo(tipo);
            assertEquals(tipo, investment.getTipo());
        }
    }

    @Test
    void testInvestmentAllStatus() {
        for (Investment.StatusInvestimento status : Investment.StatusInvestimento.values()) {
            Investment investment = new Investment();
            investment.setStatus(status);
            assertEquals(status, investment.getStatus());
        }
    }

    @Test
    void testInvestmentWithResgate() {
        Investment investment = new Investment();
        investment.setStatus(Investment.StatusInvestimento.RESGATADO);
        investment.setDataResgate(LocalDate.now());

        assertEquals(Investment.StatusInvestimento.RESGATADO, investment.getStatus());
        assertNotNull(investment.getDataResgate());
    }

    @Test
    void testInvestmentVencido() {
        Investment investment = new Investment();
        investment.setStatus(Investment.StatusInvestimento.VENCIDO);
        investment.setData(LocalDate.now().minusMonths(13));
        investment.setPrazoMeses(12);

        assertEquals(Investment.StatusInvestimento.VENCIDO, investment.getStatus());
        assertTrue(investment.getData().isBefore(LocalDate.now()));
    }
}
