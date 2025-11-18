package com.caixa.invest.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Test
    void testPrePersistSetsRoleToUserByDefault() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("test@test.com")
                .build();

        assertNull(user.getRole());
        assertNull(user.getEnabled());

        user.prePersist();

        assertEquals(User.Role.USER, user.getRole());
        assertTrue(user.getEnabled());
    }

    @Test
    void testPrePersistDoesNotOverrideExistingRole() {
        User user = User.builder()
                .username("admin")
                .password("adminpass")
                .email("admin@test.com")
                .role(User.Role.ADMIN)
                .build();

        assertEquals(User.Role.ADMIN, user.getRole());

        user.prePersist();

        assertEquals(User.Role.ADMIN, user.getRole());
    }

    @Test
    void testPrePersistSetsEnabledToTrue() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("test@test.com")
                .build();

        user.prePersist();

        assertTrue(user.getEnabled());
    }

    @Test
    void testPrePersistWithNullRole() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("test@test.com")
                .role(null)
                .build();

        user.prePersist();

        assertEquals(User.Role.USER, user.getRole());
    }

    @Test
    void testPrePersistDoesNotOverrideExistingEnabled() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("test@test.com")
                .enabled(false)
                .build();

        assertFalse(user.getEnabled());

        user.prePersist();

        assertFalse(user.getEnabled());
    }

    @Test
    void testRoleEnum() {
        assertEquals("USER", User.Role.USER.name());
        assertEquals("ADMIN", User.Role.ADMIN.name());
    }

    @Test
    void testRoleEnumValueOf() {
        User.Role role = User.Role.valueOf("ADMIN");
        assertNotNull(role);
        assertEquals(User.Role.ADMIN, role);
    }

    @Test
    void testRoleEnumValues() {
        User.Role[] roles = User.Role.values();
        assertEquals(2, roles.length);
        assertEquals(User.Role.USER, roles[0]);
        assertEquals(User.Role.ADMIN, roles[1]);
    }

    @Test
    void testGetName() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("test@test.com")
                .build();

        assertEquals("testuser", user.getName());
    }

    @Test
    void testGetRoles() {
        User user = User.builder()
                .username("admin")
                .password("password123")
                .email("admin@test.com")
                .role(User.Role.ADMIN)
                .build();

        var roles = user.getRoles();
        assertEquals(1, roles.size());
        assertTrue(roles.contains("ADMIN"));
    }
}
