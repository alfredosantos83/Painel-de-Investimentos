package com.caixa.invest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Path("/secure")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Endpoints Protegidos", description = "Endpoints que requerem autenticação JWT")
public class SecureController {

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/profile")
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Perfil do Usuário", description = "Retorna informações do usuário autenticado via JWT")
    @SecurityRequirement(name = "jwt")
    public Map<String, Object> getProfile(@Context SecurityContext ctx) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", jwt.getName());
        profile.put("email", jwt.getClaim("email"));
        profile.put("userId", jwt.getClaim("userId"));
        profile.put("roles", jwt.getGroups());
        profile.put("issuer", jwt.getIssuer());
        profile.put("expiresAt", jwt.getExpirationTime());
        profile.put("issuedAt", jwt.getIssuedAtTime());
        return profile;
    }

    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Admin Only", description = "Endpoint acessível apenas para administradores")
    @SecurityRequirement(name = "jwt")
    public Map<String, String> adminOnly() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Bem-vindo, administrador!");
        result.put("user", jwt.getName());
        result.put("access", "ADMIN");
        return result;
    }

    @GET
    @Path("/user")
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "User Area", description = "Endpoint acessível para usuários autenticados")
    @SecurityRequirement(name = "jwt")
    public Map<String, String> userArea() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Área do usuário");
        result.put("user", jwt.getName());
        result.put("access", "USER");
        return result;
    }
}
