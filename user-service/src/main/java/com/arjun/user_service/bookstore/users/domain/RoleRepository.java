package com.arjun.user_service.bookstore.users.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
