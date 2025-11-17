package com.caixa.invest.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MultivaluedMap;

import java.io.IOException;

/**
 * Filtro para permitir passar token JWT via query parameter no Swagger UI
 * Workaround para bug do Swagger UI que não envia header Authorization
 */
@Provider
@PreMatching
public class SwaggerAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();
        
        // Se houver token no query parameter e não houver Authorization header
        if (queryParams.containsKey("token") && 
            !requestContext.getHeaders().containsKey("Authorization")) {
            
            String token = queryParams.getFirst("token");
            if (token != null && !token.isEmpty()) {
                requestContext.getHeaders().putSingle("Authorization", "Bearer " + token);
            }
        }
    }
}
