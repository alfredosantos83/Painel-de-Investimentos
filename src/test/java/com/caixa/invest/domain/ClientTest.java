package com.caixa.invest.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ClientTest {

    @Test
    @Transactional
    void testClientCreation() {
        Client client = Client.builder()
                .nome("Test Client")
                .cpf("12345678901")
                .email("client@test.com")
                .perfilRisco(Client.PerfilRisco.MODERADO)
                .preferenciaInvestimento(Client.PreferenciaInvestimento.RENTABILIDADE)
                .dataCadastro(LocalDateTime.now())
                .volumeTotalInvestido(BigDecimal.valueOf(10000))
                .pontuacaoRisco(50)
                .frequenciaMovimentacoes(10)
                .build();

        client.persist();

        assertNotNull(client.id);
        assertEquals("Test Client", client.getNome());
        assertEquals("12345678901", client.getCpf());
    }

    @Test
    @Transactional
    void testClientFindByCpf() {
        Client client = Client.builder()
                .nome("CPF Test")
                .cpf("98765432100")
                .email("cpf@test.com")
                .perfilRisco(Client.PerfilRisco.CONSERVADOR)
                .dataCadastro(LocalDateTime.now())
                .build();

        client.persist();

        Client found = Client.find("cpf", "98765432100").firstResult();
        assertNotNull(found);
        assertEquals("CPF Test", found.getNome());
    }

    @Test
    @Transactional
    void testClientValidation() {
        Client client = Client.builder()
                .nome("Validation Test")
                .cpf("11111111111")
                .email("invalid-email")
                .build();

        client.prePersist();
        
        assertNotNull(client.getVolumeTotalInvestido());
        assertEquals(BigDecimal.ZERO, client.getVolumeTotalInvestido());
        assertEquals(0, client.getFrequenciaMovimentacoes());
    }
}
