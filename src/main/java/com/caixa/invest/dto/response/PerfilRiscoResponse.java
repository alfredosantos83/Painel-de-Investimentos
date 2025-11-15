package com.caixa.invest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilRiscoResponse {

    private Long clienteId;
    private String perfil;
    private Integer pontuacao;
    private String descricao;
}
