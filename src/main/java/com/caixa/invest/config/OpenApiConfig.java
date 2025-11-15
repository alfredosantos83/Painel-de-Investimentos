package com.caixa.invest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Painel de Investimentos API",
        version = "1.0.0",
        description = "API para análise de perfil de risco e simulação de investimentos",
        contact = @Contact(
            name = "Caixa Investimentos",
            email = "contato@caixa.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080/api", description = "Servidor Local"),
        @Server(url = "http://production-server:8080/api", description = "Servidor de Produção")
    }
)
@SecurityScheme(
    name = "bearer-jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
}
