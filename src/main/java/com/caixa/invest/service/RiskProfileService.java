package com.caixa.invest.service;

import com.caixa.invest.domain.Client;
import com.caixa.invest.domain.Investment;
import com.caixa.invest.repository.ClientRepository;
import com.caixa.invest.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskProfileService {

    private final ClientRepository clientRepository;
    private final InvestmentRepository investmentRepository;

    /**
     * Motor de Recomendação: Calcula perfil de risco baseado em:
     * - Volume de investimentos (peso 40)
     * - Frequência de movimentações (peso 30)
     * - Preferência por liquidez ou rentabilidade (peso 30)
     * 
     * Pontuação:
     * - Conservador: 0-40
     * - Moderado: 41-70
     * - Agressivo: 71-100
     */
    public void calcularEAtualizarPerfilRisco(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        int pontuacaoVolume = calcularPontuacaoVolume(client);
        int pontuacaoFrequencia = calcularPontuacaoFrequencia(clientId);
        int pontuacaoPreferencia = calcularPontuacaoPreferencia(clientId);

        int pontuacaoTotal = pontuacaoVolume + pontuacaoFrequencia + pontuacaoPreferencia;

        Client.PerfilRisco perfil = determinarPerfil(pontuacaoTotal);

        client.setPontuacaoRisco(pontuacaoTotal);
        client.setPerfilRisco(perfil);
        
        clientRepository.save(client);

        log.info("Perfil de risco atualizado para cliente {}: {} (pontuação: {})", 
                 clientId, perfil, pontuacaoTotal);
    }

    /**
     * Pontuação baseada no volume total investido (peso 40)
     * - Até R$ 10.000: 10 pontos
     * - R$ 10.001 a R$ 50.000: 20 pontos
     * - R$ 50.001 a R$ 100.000: 30 pontos
     * - Acima de R$ 100.000: 40 pontos
     */
    private int calcularPontuacaoVolume(Client client) {
        BigDecimal volumeTotal = client.getVolumeTotalInvestido();
        
        if (volumeTotal == null || volumeTotal.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        if (volumeTotal.compareTo(new BigDecimal("10000")) <= 0) {
            return 10;
        } else if (volumeTotal.compareTo(new BigDecimal("50000")) <= 0) {
            return 20;
        } else if (volumeTotal.compareTo(new BigDecimal("100000")) <= 0) {
            return 30;
        } else {
            return 40;
        }
    }

    /**
     * Pontuação baseada na frequência de movimentações nos últimos 12 meses (peso 30)
     * - 0-2 movimentações: 5 pontos (baixa movimentação = conservador)
     * - 3-6 movimentações: 15 pontos
     * - 7-12 movimentações: 25 pontos
     * - Mais de 12: 30 pontos (alta movimentação = agressivo)
     */
    private int calcularPontuacaoFrequencia(Long clientId) {
        LocalDate umAnoAtras = LocalDate.now().minusYears(1);
        Integer movimentacoes = investmentRepository.countMovimentacoesByClientSince(clientId, umAnoAtras);

        if (movimentacoes == null || movimentacoes == 0) {
            return 0;
        }

        if (movimentacoes <= 2) {
            return 5;
        } else if (movimentacoes <= 6) {
            return 15;
        } else if (movimentacoes <= 12) {
            return 25;
        } else {
            return 30;
        }
    }

    /**
     * Pontuação baseada em preferências de investimento (peso 30)
     * Analisa os tipos de produtos mais investidos:
     * - Produtos de baixa liquidez/alta rentabilidade: pontuação alta
     * - Produtos de alta liquidez/baixa rentabilidade: pontuação baixa
     */
    private int calcularPontuacaoPreferencia(Long clientId) {
        List<Object[]> preferencias = investmentRepository.findPreferenciasByClient(clientId);

        if (preferencias.isEmpty()) {
            return 15; // Neutro
        }

        // Pega o tipo mais investido
        Object[] maisInvestido = preferencias.get(0);
        Investment.StatusInvestimento tipo = (Investment.StatusInvestimento) maisInvestido[0];

        // Mapeia preferência para pontuação
        // CDB, LCI, LCA, POUPANCA = conservador (10 pontos)
        // FUNDO_RENDA_FIXA, TESOURO_DIRETO = moderado (20 pontos)
        // FUNDO, FUNDO_MULTIMERCADO, FUNDO_ACOES = agressivo (30 pontos)
        
        String tipoStr = tipo.toString();
        if (tipoStr.contains("CDB") || tipoStr.contains("LCI") || 
            tipoStr.contains("LCA") || tipoStr.contains("POUPANCA")) {
            return 10;
        } else if (tipoStr.contains("RENDA_FIXA") || tipoStr.contains("TESOURO")) {
            return 20;
        } else {
            return 30;
        }
    }

    /**
     * Determina perfil baseado na pontuação total
     */
    private Client.PerfilRisco determinarPerfil(int pontuacao) {
        if (pontuacao <= 40) {
            return Client.PerfilRisco.CONSERVADOR;
        } else if (pontuacao <= 70) {
            return Client.PerfilRisco.MODERADO;
        } else {
            return Client.PerfilRisco.AGRESSIVO;
        }
    }

    public String getDescricaoPerfil(Client.PerfilRisco perfil) {
        return switch (perfil) {
            case CONSERVADOR -> "Perfil focado em segurança e liquidez, com baixa tolerância a riscos.";
            case MODERADO -> "Perfil equilibrado entre segurança e rentabilidade.";
            case AGRESSIVO -> "Perfil que busca alta rentabilidade, com maior tolerância a riscos.";
        };
    }
}
