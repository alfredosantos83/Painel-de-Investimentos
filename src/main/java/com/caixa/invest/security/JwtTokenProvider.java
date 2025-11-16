package com.caixa.invest.security;

import com.caixa.invest.domain.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtTokenProvider {

    @ConfigProperty(name = "jwt.secret")
    String secret;

    @ConfigProperty(name = "jwt.expiration")
    Long expiration;

    public String generateToken(User user) {
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole().name());

        return Jwt.issuer("painel-investimentos")
                .upn(user.getUsername())
                .groups(roles)
                .claim("email", user.getEmail())
                .claim("userId", user.id)
                .expiresIn(Duration.ofMillis(expiration))
                .sign();
    }

    public String generateToken(String username, Set<String> roles) {
        return Jwt.issuer("painel-investimentos")
                .upn(username)
                .groups(roles)
                .expiresIn(Duration.ofMillis(expiration))
                .sign();
    }
}
