package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class DebugControllerTest {

    @Test
    void testListUsers() {
        given()
        .when()
            .get("/debug/users")
        .then()
            .statusCode(200)
            .body("count", greaterThanOrEqualTo(0))
            .body("users", notNullValue());
    }

    @Test
    void testListUsersContentType() {
        given()
        .when()
            .get("/debug/users")
        .then()
            .statusCode(200)
            .contentType("application/json");
    }
}
