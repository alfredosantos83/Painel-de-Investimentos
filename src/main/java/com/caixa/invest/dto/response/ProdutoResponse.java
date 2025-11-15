package com.caixa.invest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {

    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal rentabilidade;
    private String risco;
    private Integer prazoMinimoMeses;
    private Integer prazoMaximoMeses;
    private BigDecimal valorMinimo;
    private BigDecimal valorMaximo;
    private Integer liquidezDias;
    private String descricao;
}
