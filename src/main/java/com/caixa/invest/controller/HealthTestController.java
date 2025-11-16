package com.caixa.invest.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/health-test")
@Produces(MediaType.APPLICATION_JSON)
public class HealthTestController {

    @GET
    public Response healthCheck() {
        return Response.ok(Map.of(
            "status", "UP",
            "application", "Painel de Investimentos - Quarkus",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now().toString(),
            "message", "Quarkus migration in progress"
        )).build();
    }
}
