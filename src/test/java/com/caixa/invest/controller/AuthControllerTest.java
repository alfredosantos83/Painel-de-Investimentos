package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AuthControllerTest {

    @Test
    void testLoginSuccess() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("type", equalTo("Bearer"))
                .body("username", equalTo("admin"))
                .body("role", equalTo("ROLE_ADMIN"));
    }

    @Test
    void testLoginInvalidCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "wrongpassword");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void testLoginMissingUsername() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("password", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(anyOf(is(400), is(401)));
    }

    @Test
    void testLoginMissingPassword() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(anyOf(is(400), is(401)));
    }

    @Test
    void testLoginNonExistentUser() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "nonexistent");
        credentials.put("password", "anypassword");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("message", notNullValue());
    }

    @Test
    void testLoginUserRole() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "user");
        credentials.put("password", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("type", equalTo("Bearer"))
                .body("username", equalTo("user"))
                .body("role", equalTo("ROLE_USER"));
    }

    @Test
    void testLoginErrorResponseFormat() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "wrongpassword");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("message", notNullValue())
                .body("message", not(emptyString()));
    }
}
