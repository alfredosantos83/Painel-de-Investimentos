package com.caixa.invest.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserTest {

    @Test
    void testUserCreation() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(User.Role.USER)
                .enabled(true)
                .build();

        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(User.Role.USER, user.getRole());
        assertEquals(true, user.getEnabled());
    }

    @Test
    @Transactional
    void testUserPersistence() {
        User user = User.builder()
                .username("persistuser")
                .password("$2a$12$test")
                .email("persist@example.com")
                .role(User.Role.ADMIN)
                .enabled(true)
                .build();

        user.persist();

        assertNotNull(user.id);
        assertTrue(user.id > 0);
    }

    @Test
    void testFindByUsername() {
        User user = User.find("username", "admin").firstResult();
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals(User.Role.ADMIN, user.getRole());
    }

    @Test
    void testUserCount() {
        long count = User.count();
        assertTrue(count >= 2); // admin and user
    }
}
