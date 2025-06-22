package com.vcare4u.patientservice;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vcare4u.patientservice.config.JwtAuthFilter;
import com.vcare4u.patientservice.config.JwtUtils;

import org.springframework.security.core.Authentication;

class JwtAuthFilterTest {

    private JwtUtils jwtUtils;
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        jwtAuthFilter = new JwtAuthFilter(jwtUtils);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilter_NoAuthorizationHeader_ShouldSkip() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilter_WithValidToken_ShouldAuthenticate() throws ServletException, IOException {
        String token = "valid-token";
        String username = "john_doe";
        String role = "ADMIN";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtUtils.extractUsername(token)).thenReturn(username);
        when(jwtUtils.extractRole(token)).thenReturn(role);

        jwtAuthFilter.doFilter(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(username, auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testDoFilter_WithExistingAuthentication_ShouldNotOverride() throws ServletException, IOException {
        // Manually set authentication
        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.TestingAuthenticationToken("user", null)
        );

        String token = "any-token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, chain);

        // Authentication should remain unchanged
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertEquals("user", auth.getPrincipal());
    }
}
