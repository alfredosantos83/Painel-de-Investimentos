package com.caixa.invest.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    @Test
    void testTipoProdutoEnum() {
        assertEquals("CDB", Product.TipoProduto.CDB.name());
        assertEquals("LCI", Product.TipoProduto.LCI.name());
        assertEquals("LCA", Product.TipoProduto.LCA.name());
        assertEquals("TESOURO_DIRETO", Product.TipoProduto.TESOURO_DIRETO.name());
        assertEquals("FUNDO", Product.TipoProduto.FUNDO.name());
        assertEquals("FUNDO_MULTIMERCADO", Product.TipoProduto.FUNDO_MULTIMERCADO.name());
        assertEquals("FUNDO_RENDA_FIXA", Product.TipoProduto.FUNDO_RENDA_FIXA.name());
        assertEquals("FUNDO_ACOES", Product.TipoProduto.FUNDO_ACOES.name());
        assertEquals("POUPANCA", Product.TipoProduto.POUPANCA.name());
    }

    @Test
    void testTipoProdutoValueOf() {
        Product.TipoProduto tipo = Product.TipoProduto.valueOf("CDB");
        assertNotNull(tipo);
        assertEquals(Product.TipoProduto.CDB, tipo);
    }

    @Test
    void testNivelRiscoEnum() {
        assertEquals("BAIXO", Product.NivelRisco.BAIXO.name());
        assertEquals("MEDIO", Product.NivelRisco.MEDIO.name());
        assertEquals("ALTO", Product.NivelRisco.ALTO.name());
    }

    @Test
    void testNivelRiscoValueOf() {
        Product.NivelRisco risco = Product.NivelRisco.valueOf("BAIXO");
        assertNotNull(risco);
        assertEquals(Product.NivelRisco.BAIXO, risco);
    }

    @Test
    void testNivelRiscoValues() {
        Product.NivelRisco[] riscos = Product.NivelRisco.values();
        assertEquals(3, riscos.length);
        assertEquals(Product.NivelRisco.BAIXO, riscos[0]);
        assertEquals(Product.NivelRisco.MEDIO, riscos[1]);
        assertEquals(Product.NivelRisco.ALTO, riscos[2]);
    }

    @Test
    void testTipoProdutoValues() {
        Product.TipoProduto[] tipos = Product.TipoProduto.values();
        assertEquals(9, tipos.length);
    }

    @Test
    void testPrePersistSetsAtivoTrue() {
        Product product = Product.builder()
                .nome("Test Product")
                .tipo(Product.TipoProduto.CDB)
                .rentabilidade(new BigDecimal("0.10"))
                .risco(Product.NivelRisco.BAIXO)
                .build();

        assertNull(product.getAtivo());
        product.prePersist();
        assertTrue(product.getAtivo());
    }

    @Test
    void testPrePersistDoesNotOverrideAtivo() {
        Product product = Product.builder()
                .nome("Test Product")
                .tipo(Product.TipoProduto.CDB)
                .rentabilidade(new BigDecimal("0.10"))
                .risco(Product.NivelRisco.BAIXO)
                .ativo(false)
                .build();

        assertFalse(product.getAtivo());
        product.prePersist();
        assertFalse(product.getAtivo());
    }
}
