package com.caixa.invest.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProductEnhancedTest {

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setNome("CDB Test");
        product.setTipo(Product.TipoProduto.CDB);
        product.setRisco(Product.NivelRisco.BAIXO);
        product.setRentabilidade(BigDecimal.valueOf(0.12));
        product.setValorMinimo(BigDecimal.valueOf(1000));
        product.setValorMaximo(BigDecimal.valueOf(1000000));
        product.setPrazoMinimoMeses(6);
        product.setPrazoMaximoMeses(24);
        product.setLiquidezDias(90);
        product.setAtivo(true);
        product.setDescricao("Produto de teste");

        assertNotNull(product);
        assertEquals("CDB Test", product.getNome());
        assertEquals(Product.TipoProduto.CDB, product.getTipo());
        assertEquals(Product.NivelRisco.BAIXO, product.getRisco());
        assertTrue(product.getAtivo());
    }

    @Test
    void testProductAllTypes() {
        for (Product.TipoProduto tipo : Product.TipoProduto.values()) {
            Product product = new Product();
            product.setTipo(tipo);
            assertEquals(tipo, product.getTipo());
        }
    }

    @Test
    void testProductAllRisks() {
        for (Product.NivelRisco risco : Product.NivelRisco.values()) {
            Product product = new Product();
            product.setRisco(risco);
            assertEquals(risco, product.getRisco());
        }
    }

    @Test
    void testProductWithNullOptionalFields() {
        Product product = new Product();
        product.setNome("Test");
        product.setTipo(Product.TipoProduto.POUPANCA);
        product.setRisco(Product.NivelRisco.BAIXO);
        product.setRentabilidade(BigDecimal.valueOf(0.06));
        
        assertNull(product.getDescricao());
        assertNull(product.getValorMinimo());
        assertNull(product.getValorMaximo());
    }

    @Test
    void testProductActive() {
        Product product = new Product();
        product.setAtivo(true);
        assertTrue(product.getAtivo());
        
        product.setAtivo(false);
        assertFalse(product.getAtivo());
    }

    @Test
    void testProductRentabilidadeRange() {
        Product product1 = new Product();
        product1.setRentabilidade(BigDecimal.valueOf(0.05));
        
        Product product2 = new Product();
        product2.setRentabilidade(BigDecimal.valueOf(0.15));
        
        assertTrue(product1.getRentabilidade().compareTo(product2.getRentabilidade()) < 0);
    }
}
