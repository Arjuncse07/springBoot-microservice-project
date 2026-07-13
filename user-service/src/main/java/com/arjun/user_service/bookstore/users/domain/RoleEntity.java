package com.arjun.user_service.bookstore.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
class RoleEntity {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    RoleEntity() {}

    RoleEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }
}
