package com.caixa.invest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoResponse {

    private ProdutoValidado produtoValidado;
    private ResultadoSimulacao resultadoSimulacao;
    private LocalDateTime dataSimulacao;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProdutoValidado {
        private Long id;
        private String nome;
        private String tipo;
        private BigDecimal rentabilidade;
        private String risco;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultadoSimulacao {
        private BigDecimal valorFinal;
        private BigDecimal rentabilidadeEfetiva;
        private Integer prazoMeses;
        private BigDecimal impostoRenda;
        private BigDecimal valorLiquido;
    }
}
