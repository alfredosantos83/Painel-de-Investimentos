package com.caixa.invest.service;

import com.caixa.invest.domain.Client;
import com.caixa.invest.domain.Product;
import com.caixa.invest.domain.Simulation;
import com.caixa.invest.dto.request.SimulacaoRequest;
import com.caixa.invest.dto.response.SimulacaoResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@ApplicationScoped
@Slf4j
public class SimulationService {

    @Inject
    RiskProfileService riskProfileService;

    @Transactional
    public SimulacaoResponse simularInvestimento(SimulacaoRequest request) {
        // 1. Buscar e validar cliente
        Client client = Client.findById(request.getClienteId());
        if (client == null) {
            throw new RuntimeException("Cliente não encontrado");
        }

        // 2. Converter string para enum
        Product.TipoProduto tipoProduto;
        try {
            tipoProduto = Product.TipoProduto.valueOf(request.getTipoProduto().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de produto inválido: " + request.getTipoProduto());
        }

        // 3. Buscar produto que se adequa aos parâmetros
        Product product = productRepository.findByTipoAndValidParams(
            tipoProduto,
            request.getPrazoMeses(),
            request.getValor()
        ).orElseThrow(() -> new RuntimeException(
            "Nenhum produto do tipo " + tipoProduto + 
            " disponível para os parâmetros informados"
        ));

        // 4. Calcular simulação
        BigDecimal valorFinal = calcularValorFinal(
            request.getValor(),
            product.getRentabilidade(),
            request.getPrazoMeses()
        );

        BigDecimal impostoRenda = calcularImpostoRenda(
            request.getValor(),
            valorFinal,
            request.getPrazoMeses()
        );

        BigDecimal valorLiquido = valorFinal.subtract(impostoRenda);
        BigDecimal rentabilidadeEfetiva = product.getRentabilidade();

        // 5. Persistir simulação
        Simulation simulation = Simulation.builder()
            .client(client)
            .product(product)
            .valorInvestido(request.getValor())
            .valorFinal(valorFinal)
            .rentabilidadeEfetiva(rentabilidadeEfetiva)
            .prazoMeses(request.getPrazoMeses())
            .impostoRenda(impostoRenda)
            .valorLiquido(valorLiquido)
            .build();

        simulationRepository.save(simulation);

        log.info("Simulação criada para cliente {} - Produto: {} - Valor: {}", 
                 client.getId(), product.getNome(), request.getValor());

        // 6. Retornar resposta
        return SimulacaoResponse.builder()
            .produtoValidado(SimulacaoResponse.ProdutoValidado.builder()
                .id(product.getId())
                .nome(product.getNome())
                .tipo(product.getTipo().name())
                .rentabilidade(product.getRentabilidade())
                .risco(product.getRisco().name())
                .build())
            .resultadoSimulacao(SimulacaoResponse.ResultadoSimulacao.builder()
                .valorFinal(valorFinal)
                .rentabilidadeEfetiva(rentabilidadeEfetiva)
                .prazoMeses(request.getPrazoMeses())
                .impostoRenda(impostoRenda)
                .valorLiquido(valorLiquido)
                .build())
            .dataSimulacao(LocalDateTime.now())
            .build();
    }

    /**
     * Calcula valor final usando juros compostos
     * VF = VI * (1 + taxa)^prazo
     */
    private BigDecimal calcularValorFinal(BigDecimal valorInicial, 
                                          BigDecimal rentabilidadeAnual, 
                                          Integer prazoMeses) {
        // Converter rentabilidade anual para mensal
        double taxaMensal = Math.pow(1 + rentabilidadeAnual.doubleValue(), 1.0 / 12.0) - 1;
        
        // Calcular valor final com juros compostos
        double valorFinal = valorInicial.doubleValue() * Math.pow(1 + taxaMensal, prazoMeses);
        
        return BigDecimal.valueOf(valorFinal).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula Imposto de Renda regressivo sobre o rendimento
     * Até 180 dias: 22.5%
     * 181 a 360 dias: 20%
     * 361 a 720 dias: 17.5%
     * Acima de 720 dias: 15%
     */
    private BigDecimal calcularImpostoRenda(BigDecimal valorInicial, 
                                            BigDecimal valorFinal, 
                                            Integer prazoMeses) {
        BigDecimal rendimento = valorFinal.subtract(valorInicial);
        
        if (rendimento.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        int prazoDias = prazoMeses * 30;
        BigDecimal aliquota;

        if (prazoDias <= 180) {
            aliquota = new BigDecimal("0.225");
        } else if (prazoDias <= 360) {
            aliquota = new BigDecimal("0.20");
        } else if (prazoDias <= 720) {
            aliquota = new BigDecimal("0.175");
        } else {
            aliquota = new BigDecimal("0.15");
        }

        return rendimento.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
    }
}
