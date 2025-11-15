package com.caixa.invest.service;

import com.caixa.invest.domain.Client;
import com.caixa.invest.domain.Investment;
import com.caixa.invest.domain.Product;
import com.caixa.invest.repository.ClientRepository;
import com.caixa.invest.repository.InvestmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskProfileServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private RiskProfileService riskProfileService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .nome("Test Client")
                .cpf("123.456.789-00")
                .email("test@email.com")
                .volumeTotalInvestido(new BigDecimal("50000"))
                .frequenciaMovimentacoes(5)
                .preferenciaInvestimento(Client.PreferenciaInvestimento.EQUILIBRADO)
                .build();
    }

    @Test
    void testCalcularPerfilRisco_Conservador() {
        // Arrange
        client.setVolumeTotalInvestido(new BigDecimal("8000"));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(investmentRepository.countMovimentacoesByClientSince(anyLong(), any())).thenReturn(2);
        when(investmentRepository.findPreferenciasByClient(anyLong()))
                .thenReturn(Arrays.asList(new Object[]{Product.TipoProduto.CDB, 5L}));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        riskProfileService.calcularEAtualizarPerfilRisco(1L);

        // Assert
        verify(clientRepository).save(argThat(c -> 
            c.getPerfilRisco() == Client.PerfilRisco.CONSERVADOR &&
            c.getPontuacaoRisco() != null &&
            c.getPontuacaoRisco() <= 40
        ));
    }

    @Test
    void testCalcularPerfilRisco_Moderado() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(investmentRepository.countMovimentacoesByClientSince(anyLong(), any())).thenReturn(6);
        when(investmentRepository.findPreferenciasByClient(anyLong()))
                .thenReturn(Arrays.asList(new Object[]{Product.TipoProduto.FUNDO_RENDA_FIXA, 3L}));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        riskProfileService.calcularEAtualizarPerfilRisco(1L);

        // Assert
        verify(clientRepository).save(argThat(c -> 
            c.getPerfilRisco() == Client.PerfilRisco.MODERADO &&
            c.getPontuacaoRisco() > 40 &&
            c.getPontuacaoRisco() <= 70
        ));
    }

    @Test
    void testCalcularPerfilRisco_Agressivo() {
        // Arrange
        client.setVolumeTotalInvestido(new BigDecimal("150000"));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(investmentRepository.countMovimentacoesByClientSince(anyLong(), any())).thenReturn(15);
        when(investmentRepository.findPreferenciasByClient(anyLong()))
                .thenReturn(Arrays.asList(new Object[]{Product.TipoProduto.FUNDO_MULTIMERCADO, 8L}));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        riskProfileService.calcularEAtualizarPerfilRisco(1L);

        // Assert
        verify(clientRepository).save(argThat(c -> 
            c.getPerfilRisco() == Client.PerfilRisco.AGRESSIVO &&
            c.getPontuacaoRisco() > 70
        ));
    }

    @Test
    void testGetDescricaoPerfil() {
        assertEquals("Perfil focado em segurança e liquidez, com baixa tolerância a riscos.",
                riskProfileService.getDescricaoPerfil(Client.PerfilRisco.CONSERVADOR));
        assertEquals("Perfil equilibrado entre segurança e rentabilidade.",
                riskProfileService.getDescricaoPerfil(Client.PerfilRisco.MODERADO));
        assertEquals("Perfil que busca alta rentabilidade, com maior tolerância a riscos.",
                riskProfileService.getDescricaoPerfil(Client.PerfilRisco.AGRESSIVO));
    }
}
