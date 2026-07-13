package com.arjun.user_service.bookstore.users.security;

import com.arjun.user_service.bookstore.users.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, Long userId, String roleName) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.expirationMs());

        return Jwts.builder()
                .subject(username)
                .issuer("user-service")
                .claim("userId", userId)
                .claim("roles", List.of("ROLE_" + roleName))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public long getExpirationSeconds() {
        return jwtProperties.expirationMs() / 1000;
    }
}
