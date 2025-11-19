package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class HealthzControllerTest {
    @Test
    void testHealthzEndpoint() {
        given()
        .when()
            .get("/healthz")
        .then()
            .statusCode(200)
            .body(is("OK"));
    }
}
