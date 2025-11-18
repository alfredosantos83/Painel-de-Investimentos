package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class DebugControllerIntegrationTest {

    @Test
    void testGetUsers() {
        given()
            .when()
                .get("/debug/users")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", notNullValue())
                .body("users", notNullValue());
    }

    @Test
    void testGetUsersHasCorrectStructure() {
        given()
            .when()
                .get("/debug/users")
            .then()
                .statusCode(200)
                .body("users[0].username", notNullValue())
                .body("users[0].email", notNullValue())
                .body("users[0].role", notNullValue())
                .body("users[0].enabled", notNullValue())
                .body("users[0].passwordHash", notNullValue());
    }

    @Test
    void testTestPasswordWithValidUser() {
        given()
            .queryParam("username", "admin")
            .queryParam("password", "admin123")
            .when()
                .get("/debug/test-password")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("username", equalTo("admin"))
                .body("storedHash", notNullValue())
                .body("passwordMatches", notNullValue())
                .body("enabled", notNullValue());
    }

    @Test
    void testTestPasswordWithInvalidUser() {
        given()
            .queryParam("username", "nonexistent")
            .queryParam("password", "anypassword")
            .when()
                .get("/debug/test-password")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Usuário não encontrado"));
    }

    @Test
    void testTestPasswordWithWrongPassword() {
        given()
            .queryParam("username", "admin")
            .queryParam("password", "wrongpassword")
            .when()
                .get("/debug/test-password")
            .then()
                .statusCode(200)
                .body("username", equalTo("admin"))
                .body("passwordMatches", equalTo(false));
    }

    @Test
    void testGenerateHash() {
        given()
            .queryParam("password", "testpassword123")
            .when()
                .get("/debug/generate-hash")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("password", equalTo("testpassword123"))
                .body("hash", notNullValue())
                .body("hash", not(equalTo("testpassword123")));
    }

    @Test
    void testGenerateHashDifferentPasswords() {
        // First hash
        String hash1 = given()
            .queryParam("password", "password1")
            .when()
                .get("/debug/generate-hash")
            .then()
                .statusCode(200)
                .extract()
                .path("hash");

        // Second hash (same password should generate different hash due to salt)
        String hash2 = given()
            .queryParam("password", "password1")
            .when()
                .get("/debug/generate-hash")
            .then()
                .statusCode(200)
                .extract()
                .path("hash");

        // Different hashes for same password (BCrypt uses random salt)
        // Both should start with $2a$ or $2b$
        assert hash1.startsWith("$2");
        assert hash2.startsWith("$2");
    }

    @Test
    void testGenerateHashEmptyPassword() {
        given()
            .queryParam("password", "")
            .when()
                .get("/debug/generate-hash")
            .then()
                .statusCode(200)
                .body("password", equalTo(""))
                .body("hash", notNullValue());
    }
}
