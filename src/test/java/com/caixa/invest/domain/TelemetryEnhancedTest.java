package com.caixa.invest.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TelemetryEnhancedTest {

    @Test
    void testCreateTelemetry() {
        Telemetry telemetry = new Telemetry();
        telemetry.setServiceName("test-service");
        telemetry.setEndpoint("/api/test");
        telemetry.setHttpMethod("GET");
        telemetry.setHttpStatus(200);
        telemetry.setResponseTimeMs(250L);
        telemetry.setSuccess(true);
        telemetry.setTimestamp(LocalDateTime.now());

        assertNotNull(telemetry);
        assertEquals("test-service", telemetry.getServiceName());
        assertEquals("/api/test", telemetry.getEndpoint());
        assertEquals("GET", telemetry.getHttpMethod());
        assertEquals(200, telemetry.getHttpStatus());
        assertEquals(250L, telemetry.getResponseTimeMs());
        assertTrue(telemetry.getSuccess());
    }

    @Test
    void testTelemetryAllHttpMethods() {
        String[] methods = {"GET", "POST", "PUT", "DELETE", "PATCH"};
        
        for (String method : methods) {
            Telemetry telemetry = new Telemetry();
            telemetry.setHttpMethod(method);
            assertEquals(method, telemetry.getHttpMethod());
        }
    }

    @Test
    void testTelemetrySuccessScenarios() {
        int[] successCodes = {200, 201, 204};
        
        for (int code : successCodes) {
            Telemetry telemetry = new Telemetry();
            telemetry.setHttpStatus(code);
            telemetry.setSuccess(true);
            
            assertTrue(telemetry.getSuccess());
            assertTrue(telemetry.getHttpStatus() >= 200 && telemetry.getHttpStatus() < 300);
        }
    }

    @Test
    void testTelemetryErrorScenarios() {
        int[] errorCodes = {400, 401, 403, 404, 500};
        
        for (int code : errorCodes) {
            Telemetry telemetry = new Telemetry();
            telemetry.setHttpStatus(code);
            telemetry.setSuccess(false);
            
            assertFalse(telemetry.getSuccess());
            assertTrue(telemetry.getHttpStatus() >= 400);
        }
    }

    @Test
    void testTelemetryResponseTime() {
        Telemetry fast = new Telemetry();
        fast.setResponseTimeMs(50L);
        
        Telemetry slow = new Telemetry();
        slow.setResponseTimeMs(5000L);
        
        assertTrue(fast.getResponseTimeMs() < slow.getResponseTimeMs());
    }

    @Test
    void testTelemetryTimestamp() {
        Telemetry telemetry = new Telemetry();
        LocalDateTime now = LocalDateTime.now();
        telemetry.setTimestamp(now);
        
        assertEquals(now, telemetry.getTimestamp());
    }

    @Test
    void testTelemetryServiceTracking() {
        String[] services = {"auth-service", "investment-service", "simulation-service"};
        
        for (String service : services) {
            Telemetry telemetry = new Telemetry();
            telemetry.setServiceName(service);
            assertEquals(service, telemetry.getServiceName());
        }
    }
}
