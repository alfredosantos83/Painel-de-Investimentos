package com.caixa.invest.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.enterprise.context.ApplicationScoped;

@Path("/healthz")
@ApplicationScoped
public class HealthzController {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthz() {
        return Response.ok("OK").build();
    }
}
