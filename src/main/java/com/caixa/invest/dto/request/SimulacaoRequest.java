package com.caixa.invest.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoRequest {

    @NotNull(message = "clienteId é obrigatório")
    @Positive(message = "clienteId deve ser positivo")
    private Long clienteId;

    @NotNull(message = "valor é obrigatório")
    @DecimalMin(value = "0.01", message = "valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "prazoMeses é obrigatório")
    @Min(value = 1, message = "prazoMeses deve ser no mínimo 1")
    @Max(value = 360, message = "prazoMeses deve ser no máximo 360")
    private Integer prazoMeses;

    @NotBlank(message = "tipoProduto é obrigatório")
    private String tipoProduto;
}
