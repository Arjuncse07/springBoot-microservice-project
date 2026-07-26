package com.arjun.user_service.bookstore.users.domain;

final class UserMapper {

    private UserMapper() {}

    static UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getRole().getName());
    }
}
