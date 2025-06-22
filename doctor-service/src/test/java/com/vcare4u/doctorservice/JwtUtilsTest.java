package com.vcare4u.doctorservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.vcare4u.doctorservice.config.JwtUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String secret = "vcare4u1234567890vcare4u1234567890";
    private final long expiration = 86400000; // 1 day
    private final String username = "doctor123";
    private final String role = "DOCTOR";
    private Key key;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(secret, expiration);
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    @Test
    void testGenerateAndExtractUsername() {
        String token = jwtUtils.generateToken(username, role);
        assertEquals(username, jwtUtils.extractUsername(token));
    }

    @Test
    void testGenerateAndExtractRole() {
        String token = jwtUtils.generateToken(username, role);
        assertEquals(role, jwtUtils.extractRole(token));
    }

    @Test
    void testIsTokenValid_ReturnsTrue() {
        String token = jwtUtils.generateToken(username, role);
        assertTrue(jwtUtils.isTokenValid(token, username));
    }

//    @Test
//    void testIsTokenValid_ReturnsFalse_WhenExpired() {
//        String expiredToken = Jwts.builder()
//                .setSubject(username)
//                .claim("role", role)
//                .setIssuedAt(new Date(System.currentTimeMillis() - 5 * 60 * 1000)) // 5 mins ago
//                .setExpiration(new Date(System.currentTimeMillis() - 2 * 60 * 1000)) // expired 2 mins ago
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        assertFalse(jwtUtils.isTokenValid(expiredToken, username));
//    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtils.generateToken(username, role);
        Date expirationDate = jwtUtils.extractExpiration(token);
        assertTrue(expirationDate.after(new Date()));
    }
}
