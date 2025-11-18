package com.caixa.invest.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientValidationTest {

    @Test
    void testPerfilRiscoEnum() {
        assertEquals("CONSERVADOR", Client.PerfilRisco.CONSERVADOR.name());
        assertEquals("MODERADO", Client.PerfilRisco.MODERADO.name());
        assertEquals("AGRESSIVO", Client.PerfilRisco.AGRESSIVO.name());
    }

    @Test
    void testPerfilRiscoValueOf() {
        Client.PerfilRisco perfil = Client.PerfilRisco.valueOf("MODERADO");
        assertNotNull(perfil);
        assertEquals(Client.PerfilRisco.MODERADO, perfil);
    }

    @Test
    void testPerfilRiscoValues() {
        Client.PerfilRisco[] perfis = Client.PerfilRisco.values();
        assertEquals(3, perfis.length);
        assertEquals(Client.PerfilRisco.CONSERVADOR, perfis[0]);
        assertEquals(Client.PerfilRisco.MODERADO, perfis[1]);
        assertEquals(Client.PerfilRisco.AGRESSIVO, perfis[2]);
    }

    @Test
    void testPreferenciaInvestimentoEnum() {
        assertEquals("LIQUIDEZ", Client.PreferenciaInvestimento.LIQUIDEZ.name());
        assertEquals("RENTABILIDADE", Client.PreferenciaInvestimento.RENTABILIDADE.name());
        assertEquals("EQUILIBRADO", Client.PreferenciaInvestimento.EQUILIBRADO.name());
    }

    @Test
    void testPreferenciaInvestimentoValueOf() {
        Client.PreferenciaInvestimento pref = Client.PreferenciaInvestimento.valueOf("LIQUIDEZ");
        assertNotNull(pref);
        assertEquals(Client.PreferenciaInvestimento.LIQUIDEZ, pref);
    }

    @Test
    void testPreferenciaInvestimentoValues() {
        Client.PreferenciaInvestimento[] prefs = Client.PreferenciaInvestimento.values();
        assertEquals(3, prefs.length);
        assertEquals(Client.PreferenciaInvestimento.LIQUIDEZ, prefs[0]);
        assertEquals(Client.PreferenciaInvestimento.RENTABILIDADE, prefs[1]);
        assertEquals(Client.PreferenciaInvestimento.EQUILIBRADO, prefs[2]);
    }

    @Test
    void testPrePersistSetsDefaults() {
        Client client = Client.builder()
                .nome("Test Client")
                .cpf("12345678901")
                .email("test@example.com")
                .build();

        assertNull(client.getVolumeTotalInvestido());
        assertNull(client.getFrequenciaMovimentacoes());
        assertNull(client.getDataCadastro());

        client.prePersist();

        assertNotNull(client.getDataCadastro());
        assertEquals(BigDecimal.ZERO, client.getVolumeTotalInvestido());
        assertEquals(0, client.getFrequenciaMovimentacoes());
    }

    @Test
    void testPrePersistDoesNotOverrideExistingValues() {
        Client client = Client.builder()
                .nome("Test Client")
                .cpf("12345678901")
                .email("test@example.com")
                .volumeTotalInvestido(new BigDecimal("10000"))
                .frequenciaMovimentacoes(5)
                .dataCadastro(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();

        LocalDateTime originalDate = client.getDataCadastro();
        BigDecimal originalVolume = client.getVolumeTotalInvestido();
        Integer originalFrequencia = client.getFrequenciaMovimentacoes();

        client.prePersist();

        // prePersist always sets dataCadastro to now()
        assertNotEquals(originalDate, client.getDataCadastro());
        // But doesn't override volume and frequencia
        assertEquals(originalVolume, client.getVolumeTotalInvestido());
        assertEquals(originalFrequencia, client.getFrequenciaMovimentacoes());
    }

    @Test
    void testAllPerfilRiscoValues() {
        Client conservador = Client.builder().perfilRisco(Client.PerfilRisco.CONSERVADOR).build();
        Client moderado = Client.builder().perfilRisco(Client.PerfilRisco.MODERADO).build();
        Client agressivo = Client.builder().perfilRisco(Client.PerfilRisco.AGRESSIVO).build();

        assertEquals(Client.PerfilRisco.CONSERVADOR, conservador.getPerfilRisco());
        assertEquals(Client.PerfilRisco.MODERADO, moderado.getPerfilRisco());
        assertEquals(Client.PerfilRisco.AGRESSIVO, agressivo.getPerfilRisco());
    }

    @Test
    void testAllPreferenciaInvestimentoValues() {
        Client liquidez = Client.builder().preferenciaInvestimento(Client.PreferenciaInvestimento.LIQUIDEZ).build();
        Client rentabilidade = Client.builder().preferenciaInvestimento(Client.PreferenciaInvestimento.RENTABILIDADE).build();
        Client equilibrado = Client.builder().preferenciaInvestimento(Client.PreferenciaInvestimento.EQUILIBRADO).build();

        assertEquals(Client.PreferenciaInvestimento.LIQUIDEZ, liquidez.getPreferenciaInvestimento());
        assertEquals(Client.PreferenciaInvestimento.RENTABILIDADE, rentabilidade.getPreferenciaInvestimento());
        assertEquals(Client.PreferenciaInvestimento.EQUILIBRADO, equilibrado.getPreferenciaInvestimento());
    }
}
