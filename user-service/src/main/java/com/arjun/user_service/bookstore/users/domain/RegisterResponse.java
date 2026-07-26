package com.arjun.user_service.bookstore.users.domain;

public record RegisterResponse(String accessToken, String tokenType, long expiresIn, UserResponse user) {}
