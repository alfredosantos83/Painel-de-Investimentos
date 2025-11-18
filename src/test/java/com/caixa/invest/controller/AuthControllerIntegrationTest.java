package com.caixa.invest.controller;

import com.caixa.invest.dto.request.LoginRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class AuthControllerIntegrationTest {

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token", notNullValue())
                .body("type", equalTo("Bearer"))
                .body("username", equalTo("admin"))
                .body("role", equalTo("ROLE_ADMIN"));
    }

    @Test
    void testLoginWithInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .body("message", notNullValue());
    }

    @Test
    void testLoginWithNonexistentUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("anypassword");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(401)
                .body("message", notNullValue());
    }

    @Test
    void testLoginWithEmptyUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("admin123");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(400); // Validation error
    }

    @Test
    void testLoginWithEmptyPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(400); // Validation error
    }

    @Test
    void testLoginWithNullRequest() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
                .post("/auth/login")
            .then()
                .statusCode(400); // Validation error
    }

    @Test
    void testLoginReturnsValidJWT() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("password123");

        String token = given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .path("token");

        // JWT should have 3 parts separated by dots
        String[] parts = token.split("\\.");
        assert parts.length == 3;
    }

    @Test
    void testLoginWithUserRole() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("password123");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
                .post("/auth/login")
            .then()
                .statusCode(200)
                .body("username", equalTo("user"))
                .body("role", equalTo("ROLE_USER"));
    }
}
