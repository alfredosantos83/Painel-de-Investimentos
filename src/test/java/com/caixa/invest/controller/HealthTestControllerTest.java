package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class HealthTestControllerTest {

    @Test
    void testHealthEndpoint() {
        given()
        .when()
            .get("/health-test")
        .then()
            .statusCode(200)
            .body("status", is("UP"))
            .body("application", is("Painel de Investimentos - Quarkus"))
            .body("version", is("1.0.0"))
            .body("timestamp", notNullValue())
            .body("message", notNullValue());
    }

    @Test
    void testHealthEndpointContentType() {
        given()
        .when()
            .get("/health-test")
        .then()
            .statusCode(200)
            .contentType("application/json");
    }
}
