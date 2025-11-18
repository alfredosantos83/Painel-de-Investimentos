package com.caixa.invest.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TelemetryValidationTest {

    @Test
    void testPrePersistCalculatesSuccess() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("testService")
                .httpStatus(200)
                .responseTimeMs(100L)
                .build();

        assertNull(telemetry.getSuccess());
        assertNull(telemetry.getTimestamp());

        telemetry.prePersist();

        assertNotNull(telemetry.getSuccess());
        assertTrue(telemetry.getSuccess());
        assertNotNull(telemetry.getTimestamp());
    }

    @Test
    void testPrePersistSuccessWithStatus200() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("GET")
                .httpStatus(200)
                .responseTimeMs(50L)
                .build();

        telemetry.prePersist();
        assertTrue(telemetry.getSuccess());
    }

    @Test
    void testPrePersistSuccessWithStatus299() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("POST")
                .httpStatus(299)
                .responseTimeMs(80L)
                .build();

        telemetry.prePersist();
        assertTrue(telemetry.getSuccess());
    }

    @Test
    void testPrePersistFailureWithStatus199() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("GET")
                .httpStatus(199)
                .responseTimeMs(30L)
                .build();

        telemetry.prePersist();
        assertFalse(telemetry.getSuccess());
    }

    @Test
    void testPrePersistFailureWithStatus300() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("GET")
                .httpStatus(300)
                .responseTimeMs(40L)
                .build();

        telemetry.prePersist();
        assertFalse(telemetry.getSuccess());
    }

    @Test
    void testPrePersistFailureWithStatus400() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("POST")
                .httpStatus(400)
                .responseTimeMs(60L)
                .build();

        telemetry.prePersist();
        assertFalse(telemetry.getSuccess());
    }

    @Test
    void testPrePersistFailureWithStatus500() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("DELETE")
                .httpStatus(500)
                .responseTimeMs(150L)
                .build();

        telemetry.prePersist();
        assertFalse(telemetry.getSuccess());
    }

    @Test
    void testPrePersistSetsTimestamp() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("GET")
                .httpStatus(200)
                .responseTimeMs(100L)
                .build();

        LocalDateTime before = LocalDateTime.now();
        telemetry.prePersist();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(telemetry.getTimestamp());
        assertTrue(!telemetry.getTimestamp().isBefore(before));
        assertTrue(!telemetry.getTimestamp().isAfter(after));
    }

    @Test
    void testAllSuccessStatusCodes() {
        for (int statusCode = 200; statusCode < 300; statusCode++) {
            Telemetry telemetry = Telemetry.builder()
                    .serviceName("TEST")
                    .httpStatus(statusCode)
                    .responseTimeMs(100L)
                    .build();
            telemetry.prePersist();
            assertTrue(telemetry.getSuccess(), "Status code " + statusCode + " should be success");
        }
    }

    @Test
    void testTelemetryWithEndpointAndMethod() {
        Telemetry telemetry = Telemetry.builder()
                .serviceName("UserService")
                .endpoint("/api/users")
                .httpMethod("GET")
                .httpStatus(200)
                .responseTimeMs(120L)
                .build();

        telemetry.prePersist();

        assertEquals("/api/users", telemetry.getEndpoint());
        assertEquals("GET", telemetry.getHttpMethod());
        assertTrue(telemetry.getSuccess());
    }
}
