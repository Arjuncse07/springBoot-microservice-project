package com.arjun.user_service.bookstore.users.domain;

public class DuplicateUserException extends RuntimeException {

    private DuplicateUserException(String message) {
        super(message);
    }

    public static DuplicateUserException forUsername(String username) {
        return new DuplicateUserException("Username already registered: " + username);
    }

    public static DuplicateUserException forEmail(String email) {
        return new DuplicateUserException("Email already registered: " + email);
    }
}
