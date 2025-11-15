package com.caixa.invest.service;

import com.caixa.invest.domain.Client;
import com.caixa.invest.domain.Investment;
import com.caixa.invest.domain.Product;
import com.caixa.invest.domain.Simulation;
import com.caixa.invest.dto.response.*;
import com.caixa.invest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final SimulationRepository simulationRepository;
    private final InvestmentRepository investmentRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final RiskProfileService riskProfileService;

    public List<SimulacaoHistoricoResponse> buscarTodasSimulacoes() {
        return simulationRepository.findAll().stream()
            .map(this::toHistoricoResponse)
            .collect(Collectors.toList());
    }

    public List<SimulacaoHistoricoResponse> buscarSimulacoesPorCliente(Long clientId) {
        return simulationRepository.findByClientIdOrderByDataSimulacaoDesc(clientId).stream()
            .map(this::toHistoricoResponse)
            .collect(Collectors.toList());
    }

    public List<SimulacaoPorProdutoDiaResponse> buscarSimulacoesPorProdutoDia(LocalDate dataInicio, LocalDate dataFim) {
        List<Object[]> results = simulationRepository.findSimulacoesPorProdutoEDia(dataInicio, dataFim);
        
        return results.stream()
            .map(row -> SimulacaoPorProdutoDiaResponse.builder()
                .produto((String) row[0])
                .data((LocalDate) row[1])
                .quantidadeSimulacoes((Long) row[2])
                .mediaValorFinal((BigDecimal) row[3])
                .build())
            .collect(Collectors.toList());
    }

    public PerfilRiscoResponse buscarPerfilRisco(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Recalcular perfil antes de retornar
        riskProfileService.calcularEAtualizarPerfilRisco(clientId);
        
        // Buscar cliente atualizado
        client = clientRepository.findById(clientId).orElseThrow();

        return PerfilRiscoResponse.builder()
            .clienteId(client.getId())
            .perfil(client.getPerfilRisco().name())
            .pontuacao(client.getPontuacaoRisco())
            .descricao(riskProfileService.getDescricaoPerfil(client.getPerfilRisco()))
            .build();
    }

    public List<ProdutoResponse> buscarProdutosRecomendados(String perfil) {
        Client.PerfilRisco perfilRisco;
        try {
            perfilRisco = Client.PerfilRisco.valueOf(perfil.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Perfil inválido: " + perfil);
        }

        List<Product.NivelRisco> niveisRisco = getNiveisRiscoPorPerfil(perfilRisco);
        
        return productRepository.findByRiscoIn(niveisRisco).stream()
            .map(this::toProdutoResponse)
            .collect(Collectors.toList());
    }

    public List<InvestimentoResponse> buscarInvestimentos(Long clientId) {
        return investmentRepository.findByClientIdOrderByDataDesc(clientId).stream()
            .map(this::toInvestimentoResponse)
            .collect(Collectors.toList());
    }

    private List<Product.NivelRisco> getNiveisRiscoPorPerfil(Client.PerfilRisco perfil) {
        return switch (perfil) {
            case CONSERVADOR -> List.of(Product.NivelRisco.BAIXO);
            case MODERADO -> List.of(Product.NivelRisco.BAIXO, Product.NivelRisco.MEDIO);
            case AGRESSIVO -> List.of(Product.NivelRisco.MEDIO, Product.NivelRisco.ALTO);
        };
    }

    private SimulacaoHistoricoResponse toHistoricoResponse(Simulation s) {
        return SimulacaoHistoricoResponse.builder()
            .id(s.getId())
            .clienteId(s.getClient().getId())
            .produto(s.getProduct().getNome())
            .valorInvestido(s.getValorInvestido())
            .valorFinal(s.getValorFinal())
            .prazoMeses(s.getPrazoMeses())
            .dataSimulacao(s.getDataSimulacao())
            .build();
    }

    private ProdutoResponse toProdutoResponse(Product p) {
        return ProdutoResponse.builder()
            .id(p.getId())
            .nome(p.getNome())
            .tipo(p.getTipo().name())
            .rentabilidade(p.getRentabilidade())
            .risco(p.getRisco().name())
            .prazoMinimoMeses(p.getPrazoMinimoMeses())
            .prazoMaximoMeses(p.getPrazoMaximoMeses())
            .valorMinimo(p.getValorMinimo())
            .valorMaximo(p.getValorMaximo())
            .liquidezDias(p.getLiquidezDias())
            .descricao(p.getDescricao())
            .build();
    }

    private InvestimentoResponse toInvestimentoResponse(Investment i) {
        return InvestimentoResponse.builder()
            .id(i.getId())
            .tipo(i.getTipo().name())
            .valor(i.getValor())
            .rentabilidade(i.getRentabilidade())
            .data(i.getData())
            .prazoMeses(i.getPrazoMeses())
            .status(i.getStatus().name())
            .build();
    }
}
