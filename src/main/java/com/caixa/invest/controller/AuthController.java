package com.caixa.invest.controller;

import com.caixa.invest.domain.User;
import com.caixa.invest.dto.request.LoginRequest;
import com.caixa.invest.dto.response.AuthResponse;
import com.caixa.invest.security.JwtTokenProvider;
import com.caixa.invest.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @Inject
    JwtTokenProvider tokenProvider;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        try {
            User user = authService.authenticate(request.getUsername(), request.getPassword());
            String token = tokenProvider.generateToken(user);

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .username(user.getUsername())
                    .role("ROLE_" + user.getRole().name())
                    .build();

            return Response.ok(response).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
