package com.caixa.invest.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends PanacheEntity implements Principal {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean enabled;

    @PrePersist
    public void prePersist() {
        if (this.enabled == null) {
            this.enabled = true;
        }
        if (this.role == null) {
            this.role = Role.USER;
        }
    }

    @Override
    public String getName() {
        return username;
    }

    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        roles.add(role.name());
        return roles;
    }

    public enum Role {
        USER,
        ADMIN
    }
}
