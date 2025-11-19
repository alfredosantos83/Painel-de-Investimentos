package com.caixa.invest.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ProductControllerTest {

    @Test
    public void testGetProductsAuthorized() {
        String token = given()
            .contentType("application/json")
            .body("{\"username\":\"admin\",\"password\":\"password123\"}")
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .extract().path("token");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/api/products/all")
        .then()
            .statusCode(200);
    }
}
