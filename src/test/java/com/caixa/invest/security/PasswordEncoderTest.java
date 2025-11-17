package com.caixa.invest.security;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PasswordEncoderTest {

    @Inject
    PasswordEncoder passwordEncoder;

    @Test
    void testEncode() {
        String rawPassword = "myPassword123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        assertTrue(encoded.startsWith("$2a$")); // BCrypt format
    }

    @Test
    void testMatches() {
        String rawPassword = "testPassword123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }

    @Test
    void testMatchesWithWrongPassword() {
        String rawPassword = "correctPassword";
        String encoded = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.matches("wrongPassword", encoded));
    }

    @Test
    void testEncodeDifferentPasswordsProduceDifferentHashes() {
        String password1 = "password1";
        String password2 = "password2";

        String hash1 = passwordEncoder.encode(password1);
        String hash2 = passwordEncoder.encode(password2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testEncodeSamePasswordProducesDifferentHashes() {
        String password = "samePassword";

        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // BCrypt produces different hashes for same password (salt)
        assertNotEquals(hash1, hash2);
        // But both should verify correctly
        assertTrue(passwordEncoder.matches(password, hash1));
        assertTrue(passwordEncoder.matches(password, hash2));
    }

    @Test
    void testMatchesWithEmptyPassword() {
        String encoded = passwordEncoder.encode("");
        assertTrue(passwordEncoder.matches("", encoded));
    }

    @Test
    void testMatchesWithSpecialCharacters() {
        String password = "p@$$w0rd!#%&*()";
        String encoded = passwordEncoder.encode(password);

        assertTrue(passwordEncoder.matches(password, encoded));
        assertFalse(passwordEncoder.matches("p@$$w0rd", encoded));
    }
}
