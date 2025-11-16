package com.caixa.invest.controller;

import com.caixa.invest.domain.User;
import com.caixa.invest.security.JwtTokenProvider;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class SecureControllerTest {

    @Inject
    JwtTokenProvider jwtTokenProvider;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() {
        // Gera tokens JWT reais para os testes
        User admin = User.find("username", "admin").firstResult();
        User user = User.find("username", "user").firstResult();
        
        adminToken = jwtTokenProvider.generateToken(admin);
        userToken = jwtTokenProvider.generateToken(user);
    }

    // Testes SEM autenticação (401 Unauthorized)
    @Test
    void testProfileEndpointUnauthorized() {
        given()
        .when()
            .get("/secure/profile")
        .then()
            .statusCode(401);
    }

    @Test
    void testAdminEndpointUnauthorized() {
        given()
        .when()
            .get("/secure/admin")
        .then()
            .statusCode(401);
    }

    @Test
    void testUserEndpointUnauthorized() {
        given()
        .when()
            .get("/secure/user")
        .then()
            .statusCode(401);
    }

    // Testes COM autenticação (200 OK)
    @Test
    void testProfileEndpointWithAdminToken() {
        given()
            .header("Authorization", "Bearer " + adminToken)
        .when()
            .get("/secure/profile")
        .then()
            .statusCode(200)
            .body("username", equalTo("admin"))
            .body("email", equalTo("admin@caixa.com"))
            .body("roles", hasItem("ADMIN"));
    }

    @Test
    void testProfileEndpointWithUserToken() {
        given()
            .header("Authorization", "Bearer " + userToken)
        .when()
            .get("/secure/profile")
        .then()
            .statusCode(200)
            .body("username", equalTo("user"))
            .body("email", equalTo("user@caixa.com"))
            .body("roles", hasItem("USER"));
    }

    @Test
    void testAdminEndpointWithAdminToken() {
        given()
            .header("Authorization", "Bearer " + adminToken)
        .when()
            .get("/secure/admin")
        .then()
            .statusCode(200)
            .body("message", equalTo("Bem-vindo, administrador!"))
            .body("user", equalTo("admin"))
            .body("access", equalTo("ADMIN"));
    }

    @Test
    void testAdminEndpointWithUserToken() {
        // Usuário comum não deve acessar endpoint ADMIN (403 Forbidden)
        given()
            .header("Authorization", "Bearer " + userToken)
        .when()
            .get("/secure/admin")
        .then()
            .statusCode(403);
    }

    @Test
    void testUserEndpointWithAdminToken() {
        given()
            .header("Authorization", "Bearer " + adminToken)
        .when()
            .get("/secure/user")
        .then()
            .statusCode(200)
            .body("message", equalTo("Área do usuário"))
            .body("user", equalTo("admin"))
            .body("access", equalTo("USER"));
    }

    @Test
    void testUserEndpointWithUserToken() {
        given()
            .header("Authorization", "Bearer " + userToken)
        .when()
            .get("/secure/user")
        .then()
            .statusCode(200)
            .body("message", equalTo("Área do usuário"))
            .body("user", equalTo("user"))
            .body("access", equalTo("USER"));
    }

    @Test
    void testInvalidToken() {
        given()
            .header("Authorization", "Bearer invalid-token-xyz")
        .when()
            .get("/secure/profile")
        .then()
            .statusCode(401);
    }

    @Test
    void testMalformedAuthorizationHeader() {
        given()
            .header("Authorization", "InvalidFormat " + adminToken)
        .when()
            .get("/secure/profile")
        .then()
            .statusCode(401);
    }
}
