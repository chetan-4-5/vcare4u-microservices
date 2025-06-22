package com.vcare4u.appointmentservice;

import com.vcare4u.appointmentservice.config.JwtUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private final JwtUtils jwtUtils = new JwtUtils();

    @Test
    void testValidToken() {
        String token = createTestToken("user@example.com", "PATIENT");

        assertTrue(jwtUtils.isTokenValid(token));
        assertEquals("user@example.com", jwtUtils.extractUsername(token));
        assertEquals("PATIENT", jwtUtils.extractRole(token));
    }

    @Test
    void testInvalidToken() {
        assertFalse(jwtUtils.isTokenValid("invalid.token.value"));
    }

    private String createTestToken(String username, String role) {
        // Create token using internal method if possible
        // or just use a pre-generated valid token for test
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                        "vcare4u1234567890vcare4u1234567890".getBytes()
                ))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 1000000))
                .compact();
    }
}