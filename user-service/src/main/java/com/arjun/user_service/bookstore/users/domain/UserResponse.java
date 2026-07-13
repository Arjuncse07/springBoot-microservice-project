package com.arjun.user_service.bookstore.users.domain;

public record UserResponse(
        Long id, String username, String email, String firstName, String lastName, String fullName, String role) {}
