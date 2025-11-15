package com.caixa.invest.controller;

import com.caixa.invest.dto.request.SimulacaoRequest;
import com.caixa.invest.dto.response.*;
import com.caixa.invest.service.QueryService;
import com.caixa.invest.service.SimulationService;
import com.caixa.invest.service.TelemetryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Investimentos", description = "APIs para simulação e consulta de investimentos")
@SecurityRequirement(name = "bearer-jwt")
public class InvestmentController {

    private final SimulationService simulationService;
    private final QueryService queryService;
    private final TelemetryService telemetryService;

    @PostMapping("/simular-investimento")
    @Operation(summary = "Simular investimento", description = "Realiza simulação de investimento com base nos parâmetros fornecidos")
    public ResponseEntity<SimulacaoResponse> simularInvestimento(@Valid @RequestBody SimulacaoRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            SimulacaoResponse response = simulationService.simularInvestimento(request);
            
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("simular-investimento", "/simular-investimento", "POST", responseTime, 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("simular-investimento", "/simular-investimento", "POST", responseTime, 500);
            throw e;
        }
    }

    @GetMapping("/simulacoes")
    @Operation(summary = "Histórico de simulações", description = "Retorna todas as simulações realizadas")
    public ResponseEntity<List<SimulacaoHistoricoResponse>> buscarSimulacoes(
            @RequestParam(required = false) Long clienteId) {
        
        if (clienteId != null) {
            return ResponseEntity.ok(queryService.buscarSimulacoesPorCliente(clienteId));
        }
        return ResponseEntity.ok(queryService.buscarTodasSimulacoes());
    }

    @GetMapping("/simulacoes/por-produto-dia")
    @Operation(summary = "Simulações por produto e dia", description = "Retorna valores simulados agrupados por produto e dia")
    public ResponseEntity<List<SimulacaoPorProdutoDiaResponse>> buscarSimulacoesPorProdutoDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        return ResponseEntity.ok(queryService.buscarSimulacoesPorProdutoDia(dataInicio, dataFim));
    }

    @GetMapping("/perfil-risco/{clienteId}")
    @Operation(summary = "Perfil de risco do cliente", description = "Retorna perfil de risco calculado para o cliente")
    public ResponseEntity<PerfilRiscoResponse> buscarPerfilRisco(@PathVariable Long clienteId) {
        long startTime = System.currentTimeMillis();
        
        try {
            PerfilRiscoResponse response = queryService.buscarPerfilRisco(clienteId);
            
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("perfil-risco", "/perfil-risco/" + clienteId, "GET", responseTime, 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("perfil-risco", "/perfil-risco/" + clienteId, "GET", responseTime, 500);
            throw e;
        }
    }

    @GetMapping("/produtos-recomendados/{perfil}")
    @Operation(summary = "Produtos recomendados", description = "Retorna produtos adequados ao perfil de risco")
    public ResponseEntity<List<ProdutoResponse>> buscarProdutosRecomendados(@PathVariable String perfil) {
        return ResponseEntity.ok(queryService.buscarProdutosRecomendados(perfil));
    }

    @GetMapping("/investimentos/{clienteId}")
    @Operation(summary = "Histórico de investimentos", description = "Retorna histórico de investimentos do cliente")
    public ResponseEntity<List<InvestimentoResponse>> buscarInvestimentos(@PathVariable Long clienteId) {
        return ResponseEntity.ok(queryService.buscarInvestimentos(clienteId));
    }

    @GetMapping("/telemetria")
    @Operation(summary = "Dados de telemetria", description = "Retorna dados de telemetria dos serviços")
    public ResponseEntity<TelemetriaResponse> buscarTelemetria(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        LocalDate dataInicio = inicio != null ? inicio : LocalDate.now().minusMonths(1);
        LocalDate dataFim = fim != null ? fim : LocalDate.now();
        
        return ResponseEntity.ok(telemetryService.buscarTelemetria(dataInicio, dataFim));
    }
}
