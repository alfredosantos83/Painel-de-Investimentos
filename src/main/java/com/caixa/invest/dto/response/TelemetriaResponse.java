package com.caixa.invest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetriaResponse {

    private List<ServicoTelemetria> servicos;
    private Periodo periodo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicoTelemetria {
        private String nome;
        private Long quantidadeChamadas;
        private Long mediaTempoRespostaMs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Periodo {
        private LocalDate inicio;
        private LocalDate fim;
    }
}
