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
public class InvestimentoResponse {

    private Long id;
    private String tipo;
    private BigDecimal valor;
    private BigDecimal rentabilidade;
    private LocalDate data;
    private Integer prazoMeses;
    private String status;
}
