package com.vcare4u.patientservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vcare4u.patientservice.config.JwtUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String token;
    private final String username = "testuser";
    private final String role = "ADMIN";

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        // Create a valid JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(Keys.hmacShaKeyFor("vcare4u1234567890vcare4u1234567890".getBytes()))
                .compact();
    }

    @Test
    void testExtractUsername() {
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractRole() {
        String extractedRole = jwtUtils.extractRole(token);
        assertEquals(role, extractedRole);
    }

    @Test
    void testIsTokenValid() {
        boolean isValid = jwtUtils.isTokenValid(token, username);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenInvalidForWrongUser() {
        boolean isValid = jwtUtils.isTokenValid(token, "otheruser");
        assertFalse(isValid);
    }

    @Test
    void testExtractTokenFromRequest() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        String extractedToken = jwtUtils.extractToken(mockRequest);
        assertEquals(token, extractedToken);
    }

    @Test
    void testExtractUsernameFromRequest() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        String extractedUsername = jwtUtils.extractUsernameFromRequest(mockRequest);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractRoleFromRequest() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        String extractedRole = jwtUtils.extractRoleFromRequest(mockRequest);
        assertEquals(role, extractedRole);
    }
}
