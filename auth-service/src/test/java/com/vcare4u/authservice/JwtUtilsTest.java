package com.vcare4u.authservice;

import com.vcare4u.authservice.config.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String testUsername;
    private String testRole;
    private Long testId;
    private String token;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(); // No reflection

        testUsername = "testuser";
        testRole = "PATIENT";
        testId = 1L;

        token = jwtUtils.generateToken(testUsername, testRole, testId);
    }

    @Test
    void testGenerateToken_NotNull() {
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String username = jwtUtils.extractUsername(token);
        assertEquals(testUsername, username);
    }

    @Test
    void testExtractRole() {
        String role = jwtUtils.extractRole(token);
        assertEquals(testRole, role);
    }

    @Test
    void testExtractExpiration_NotExpired() {
        Date expiration = jwtUtils.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testIsTokenValid_TrueForValidToken() {
        assertTrue(jwtUtils.isTokenValid(token, testUsername));
    }

    @Test
    void testIsTokenValid_FalseForInvalidUsername() {
        assertFalse(jwtUtils.isTokenValid(token, "wronguser"));
    }

    @Test
    void testIsTokenValid_FalseForExpiredToken() throws Exception {
        // Reflectively invoke private getSigningKey() method
        Method getSigningKey = JwtUtils.class.getDeclaredMethod("getSigningKey");
        getSigningKey.setAccessible(true);
        Key signingKey = (Key) getSigningKey.invoke(jwtUtils);

        Date now = new Date();
        Date expiredTime = new Date(now.getTime() - 10000); // 10 sec ago
        Date issuedAt = new Date(now.getTime() - 20000);    // 20 sec ago

        String expiredToken = Jwts.builder()
                .setSubject(testUsername)
                .claim("role", testRole)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredTime)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        boolean isValid;
        try {
            isValid = jwtUtils.isTokenValid(expiredToken, testUsername);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            isValid = false; // Treat it as invalid
        }
        assertFalse(isValid, "Token should be invalid because it is expired.");

    }
}
