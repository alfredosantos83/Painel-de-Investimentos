package com.caixa.invest.service;

import com.caixa.invest.domain.Telemetry;
import com.caixa.invest.dto.response.TelemetriaResponse;
import com.caixa.invest.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;

    public void registrar(String serviceName, String endpoint, String httpMethod, 
                         Long responseTimeMs, Integer httpStatus) {
        Telemetry telemetry = Telemetry.builder()
            .serviceName(serviceName)
            .endpoint(endpoint)
            .httpMethod(httpMethod)
            .responseTimeMs(responseTimeMs)
            .httpStatus(httpStatus)
            .build();

        telemetryRepository.save(telemetry);
    }

    public TelemetriaResponse buscarTelemetria(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        List<Object[]> results = telemetryRepository.findTelemetriaByPeriodo(inicio, fim);

        List<TelemetriaResponse.ServicoTelemetria> servicos = results.stream()
            .map(row -> TelemetriaResponse.ServicoTelemetria.builder()
                .nome((String) row[0])
                .quantidadeChamadas((Long) row[1])
                .mediaTempoRespostaMs(((Double) row[2]).longValue())
                .build())
            .collect(Collectors.toList());

        return TelemetriaResponse.builder()
            .servicos(servicos)
            .periodo(TelemetriaResponse.Periodo.builder()
                .inicio(dataInicio)
                .fim(dataFim)
                .build())
            .build();
    }
}
