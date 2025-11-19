package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DebugControllerEnhancedTest {

    @Test
    public void testGetUsers() {
        given()
            .when()
            .get("/debug/users")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("count", greaterThanOrEqualTo(2))
            .body("users", notNullValue())
            .body("users[0].username", notNullValue())
            .body("users[0].email", notNullValue())
            .body("users[0].role", notNullValue())
            .body("users[0].enabled", notNullValue())
            .body("users[0].passwordHash", containsString("..."));
    }

    @Test
    public void testTestPasswordWithValidUser() {
        given()
            .queryParam("username", "admin")
            .queryParam("password", "password123")
            .when()
            .get("/debug/test-password")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("username", equalTo("admin"))
            .body("storedHash", notNullValue())
            .body("storedHash", containsString("..."))
            .body("passwordMatches", equalTo(true))
            .body("enabled", equalTo(true));
    }

    @Test
    public void testTestPasswordWithInvalidPassword() {
        given()
            .queryParam("username", "admin")
            .queryParam("password", "wrongpassword")
            .when()
            .get("/debug/test-password")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("username", equalTo("admin"))
            .body("passwordMatches", equalTo(false));
    }

    @Test
    public void testTestPasswordWithNonExistentUser() {
        given()
            .queryParam("username", "nonexistent")
            .queryParam("password", "anypassword")
            .when()
            .get("/debug/test-password")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("error", equalTo("Usuário não encontrado"))
            .body("username", nullValue());
    }

    @Test
    public void testGenerateHash() {
        given()
            .queryParam("password", "mySecretPassword123")
            .when()
            .get("/debug/generate-hash")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("password", equalTo("mySecretPassword123"))
            .body("hash", notNullValue())
            .body("hash", startsWith("$2a$"));  // BCrypt hash format
    }

    @Test
    public void testGenerateHashWithEmptyPassword() {
        given()
            .queryParam("password", "")
            .when()
            .get("/debug/generate-hash")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("password", equalTo(""))
            .body("hash", notNullValue());
    }

    @Test
    void testPasswordMatchSuccess() {
        given()
            .queryParam("username", "admin")
            .queryParam("password", "password123")
        .when()
            .get("/debug/test-password")
        .then()
            .statusCode(200)
            .body("passwordMatches", is(true));
    }

    @Test
    void testPasswordMatchFail() {
        given()
            .queryParam("username", "admin")
            .queryParam("password", "wrongpass")
        .when()
            .get("/debug/test-password")
        .then()
            .statusCode(200)
            .body("passwordMatches", is(false));
    }

    @Test
    void testPasswordUserNotFound() {
        given()
            .queryParam("username", "notfound")
            .queryParam("password", "any")
        .when()
            .get("/debug/test-password")
        .then()
            .statusCode(200)
            .body("error", is("Usuário não encontrado"));
    }
}
