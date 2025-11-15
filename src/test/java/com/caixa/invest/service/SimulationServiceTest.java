package com.caixa.invest.service;

import com.caixa.invest.domain.Client;
import com.caixa.invest.domain.Product;
import com.caixa.invest.dto.request.SimulacaoRequest;
import com.caixa.invest.dto.response.SimulacaoResponse;
import com.caixa.invest.repository.ClientRepository;
import com.caixa.invest.repository.ProductRepository;
import com.caixa.invest.repository.SimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private RiskProfileService riskProfileService;

    @InjectMocks
    private SimulationService simulationService;

    private Client client;
    private Product product;
    private SimulacaoRequest request;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .nome("Test Client")
                .cpf("123.456.789-00")
                .email("test@email.com")
                .build();

        product = Product.builder()
                .id(1L)
                .nome("CDB Test")
                .tipo(Product.TipoProduto.CDB)
                .rentabilidade(new BigDecimal("0.12"))
                .risco(Product.NivelRisco.BAIXO)
                .prazoMinimoMeses(6)
                .prazoMaximoMeses(24)
                .valorMinimo(new BigDecimal("1000"))
                .valorMaximo(new BigDecimal("1000000"))
                .build();

        request = SimulacaoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("10000"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();
    }

    @Test
    void testSimularInvestimento_Success() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findByTipoAndValidParams(any(), anyInt(), any()))
                .thenReturn(Optional.of(product));
        when(simulationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SimulacaoResponse response = simulationService.simularInvestimento(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getProdutoValidado());
        assertNotNull(response.getResultadoSimulacao());
        assertEquals("CDB Test", response.getProdutoValidado().getNome());
        assertTrue(response.getResultadoSimulacao().getValorFinal()
                .compareTo(request.getValor()) > 0);
        
        verify(simulationRepository, times(1)).save(any());
    }

    @Test
    void testSimularInvestimento_ClienteNaoEncontrado() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            simulationService.simularInvestimento(request);
        });
    }

    @Test
    void testSimularInvestimento_ProdutoNaoEncontrado() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findByTipoAndValidParams(any(), anyInt(), any()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            simulationService.simularInvestimento(request);
        });
    }

    @Test
    void testSimularInvestimento_TipoProdutoInvalido() {
        // Arrange
        request.setTipoProduto("INVALID_TYPE");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            simulationService.simularInvestimento(request);
        });
    }
}
