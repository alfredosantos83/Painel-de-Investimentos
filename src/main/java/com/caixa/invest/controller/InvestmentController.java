package com.caixa.invest.controller;

import com.caixa.invest.dto.request.SimulacaoRequest;
import com.caixa.invest.dto.response.*;
import com.caixa.invest.service.QueryService;
import com.caixa.invest.service.SimulationService;
import com.caixa.invest.service.TelemetryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Investimentos", description = "APIs para simulação e consulta de investimentos")
@SecurityRequirement(name = "bearer-jwt")
@RolesAllowed({"USER", "ADMIN"})
public class InvestmentController {

    @Inject
    SimulationService simulationService;

    @Inject
    QueryService queryService;

    @Inject
    TelemetryService telemetryService;

    @POST
    @Path("/simular-investimento")
    @Operation(summary = "Simular investimento", description = "Realiza simulação de investimento com base nos parâmetros fornecidos")
    public Response simularInvestimento(@Valid SimulacaoRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            SimulacaoResponse response = simulationService.simularInvestimento(request);
            
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("simular-investimento", "/simular-investimento", "POST", responseTime, 200);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("simular-investimento", "/simular-investimento", "POST", responseTime, 500);
            throw e;
        }
    }

    @GET
    @Path("/simulacoes")
    @Operation(summary = "Histórico de simulações", description = "Retorna todas as simulações realizadas")
    public Response buscarSimulacoes(@QueryParam("clienteId") Long clienteId) {
        List<SimulacaoHistoricoResponse> result;
        
        if (clienteId != null) {
            result = queryService.buscarSimulacoesPorCliente(clienteId);
        } else {
            result = queryService.buscarTodasSimulacoes();
        }
        
        return Response.ok(result).build();
    }

    @GET
    @Path("/simulacoes/por-produto-dia")
    @Operation(summary = "Simulações por produto e dia", description = "Retorna valores simulados agrupados por produto e dia")
    public Response buscarSimulacoesPorProdutoDia(
            @QueryParam("dataInicio") String dataInicioStr,
            @QueryParam("dataFim") String dataFimStr) {
        
        LocalDate dataInicio = LocalDate.parse(dataInicioStr);
        LocalDate dataFim = LocalDate.parse(dataFimStr);
        
        List<SimulacaoPorProdutoDiaResponse> result = queryService.buscarSimulacoesPorProdutoDia(dataInicio, dataFim);
        return Response.ok(result).build();
    }

    @GET
    @Path("/perfil-risco/{clienteId}")
    @Operation(summary = "Perfil de risco do cliente", description = "Retorna perfil de risco calculado para o cliente")
    public Response buscarPerfilRisco(@PathParam("clienteId") Long clienteId) {
        long startTime = System.currentTimeMillis();
        
        try {
            PerfilRiscoResponse response = queryService.buscarPerfilRisco(clienteId);
            
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("perfil-risco", "/perfil-risco/" + clienteId, "GET", responseTime, 200);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            telemetryService.registrar("perfil-risco", "/perfil-risco/" + clienteId, "GET", responseTime, 500);
            throw e;
        }
    }

    @GET
    @Path("/produtos-recomendados/{perfil}")
    @Operation(summary = "Produtos recomendados", description = "Retorna produtos adequados ao perfil de risco")
    public Response buscarProdutosRecomendados(@PathParam("perfil") String perfil) {
        List<ProdutoResponse> result = queryService.buscarProdutosRecomendados(perfil);
        return Response.ok(result).build();
    }

    @GET
    @Path("/investimentos/{clienteId}")
    @Operation(summary = "Histórico de investimentos", description = "Retorna histórico de investimentos do cliente")
    public Response buscarInvestimentos(@PathParam("clienteId") Long clienteId) {
        List<InvestimentoResponse> result = queryService.buscarInvestimentos(clienteId);
        return Response.ok(result).build();
    }

    @GET
    @Path("/telemetria")
    @Operation(summary = "Dados de telemetria", description = "Retorna dados de telemetria dos serviços")
    public Response buscarTelemetria(
            @QueryParam("inicio") String inicioStr,
            @QueryParam("fim") String fimStr) {
        
        LocalDate dataInicio = inicioStr != null ? LocalDate.parse(inicioStr) : LocalDate.now().minusMonths(1);
        LocalDate dataFim = fimStr != null ? LocalDate.parse(fimStr) : LocalDate.now();
        
        TelemetriaResponse result = telemetryService.buscarTelemetria(dataInicio, dataFim);
        return Response.ok(result).build();
    }
}
