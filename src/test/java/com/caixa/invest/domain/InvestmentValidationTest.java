package com.caixa.invest.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentValidationTest {

    @Test
    void testStatusInvestimentoEnum() {
        assertEquals("ATIVO", Investment.StatusInvestimento.ATIVO.name());
        assertEquals("RESGATADO", Investment.StatusInvestimento.RESGATADO.name());
        assertEquals("VENCIDO", Investment.StatusInvestimento.VENCIDO.name());
    }

    @Test
    void testStatusInvestimentoValueOf() {
        Investment.StatusInvestimento status = Investment.StatusInvestimento.valueOf("ATIVO");
        assertNotNull(status);
        assertEquals(Investment.StatusInvestimento.ATIVO, status);
    }

    @Test
    void testStatusInvestimentoValues() {
        Investment.StatusInvestimento[] statuses = Investment.StatusInvestimento.values();
        assertEquals(3, statuses.length);
        assertEquals(Investment.StatusInvestimento.ATIVO, statuses[0]);
        assertEquals(Investment.StatusInvestimento.RESGATADO, statuses[1]);
        assertEquals(Investment.StatusInvestimento.VENCIDO, statuses[2]);
    }

    @Test
    void testInvestmentBuilder() {
        Investment investment = Investment.builder()
                .tipo(Product.TipoProduto.CDB)
                .status(Investment.StatusInvestimento.ATIVO)
                .data(LocalDate.now())
                .build();

        assertNotNull(investment);
        assertEquals(Product.TipoProduto.CDB, investment.getTipo());
        assertEquals(Investment.StatusInvestimento.ATIVO, investment.getStatus());
        assertNotNull(investment.getData());
    }

    @Test
    void testInvestmentAllStatuses() {
        Investment ativo = Investment.builder().status(Investment.StatusInvestimento.ATIVO).build();
        Investment resgatado = Investment.builder().status(Investment.StatusInvestimento.RESGATADO).build();
        Investment vencido = Investment.builder().status(Investment.StatusInvestimento.VENCIDO).build();

        assertEquals(Investment.StatusInvestimento.ATIVO, ativo.getStatus());
        assertEquals(Investment.StatusInvestimento.RESGATADO, resgatado.getStatus());
        assertEquals(Investment.StatusInvestimento.VENCIDO, vencido.getStatus());
    }
}
