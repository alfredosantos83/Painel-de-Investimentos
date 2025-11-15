package com.caixa.invest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoPorProdutoDiaResponse {

    private String produto;
    private LocalDate data;
    private Long quantidadeSimulacoes;
    private BigDecimal mediaValorFinal;
}
