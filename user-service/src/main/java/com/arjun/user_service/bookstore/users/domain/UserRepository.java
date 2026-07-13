package com.arjun.user_service.bookstore.users.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByUsername(String username);
}
