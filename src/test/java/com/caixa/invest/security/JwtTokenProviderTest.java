package com.caixa.invest.security;

import com.caixa.invest.domain.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class JwtTokenProviderTest {

    @Inject
    JwtTokenProvider jwtTokenProvider;

    @Test
    void testGenerateToken() {
        // Busca usuário existente do data.sql
        User user = User.find("username", "admin").firstResult();
        assertNotNull(user, "User admin deve existir no banco");

        String token = jwtTokenProvider.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.startsWith("eyJ")); // JWT format
    }

    @Test
    void testGenerateTokenForAdmin() {
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        
        String token = jwtTokenProvider.generateToken("admin", roles);

        assertNotNull(token);
        assertTrue(token.length() > 100);
    }

    @Test
    void testGenerateTokenWithDifferentUsers() {
        // Busca usuários existentes do data.sql
        User user1 = User.find("username", "admin").firstResult();
        User user2 = User.find("username", "user").firstResult();
        
        assertNotNull(user1, "User admin deve existir no banco");
        assertNotNull(user2, "User user deve existir no banco");

        String token1 = jwtTokenProvider.generateToken(user1);
        String token2 = jwtTokenProvider.generateToken(user2);

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }
}
