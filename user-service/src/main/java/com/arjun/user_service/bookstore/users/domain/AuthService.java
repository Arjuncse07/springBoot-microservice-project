package com.arjun.user_service.bookstore.users.domain;

import com.arjun.user_service.bookstore.users.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String DEFAULT_ROLE = "USER";
    private static final String LOCAL_PROVIDER = "LOCAL";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw DuplicateUserException.forUsername(request.username());
        }
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw DuplicateUserException.forEmail(request.email());
        }

        RoleEntity role = roleRepository
                .findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role not found: " + DEFAULT_ROLE));

        UserEntity user = new UserEntity();
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setFirstName(request.firstName().trim());
        user.setMiddleName(trimToNull(request.middleName()));
        user.setLastName(request.lastName().trim());
        user.setFullName(buildFullName(request.firstName(), request.middleName(), request.lastName()));
        user.setPhone(trimToNull(request.phone()));
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setAuthProvider(LOCAL_PROVIDER);
        user.setRole(role);
        user.setEnabled(true);

        UserEntity savedUser = userRepository.save(user);
        String accessToken = jwtService.generateAccessToken(
                savedUser.getUsername(), savedUser.getId(), savedUser.getRole().getName());

        return new RegisterResponse(
                accessToken, "Bearer", jwtService.getExpirationSeconds(), UserMapper.toResponse(savedUser));
    }

    private static String buildFullName(String firstName, String middleName, String lastName) {
        String middle = trimToNull(middleName);
        if (middle == null) {
            return firstName.trim() + " " + lastName.trim();
        }
        return firstName.trim() + " " + middle + " " + lastName.trim();
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
