package com.vcare4u.authservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.vcare4u.authservice.config.JwtAuthFilter;
import com.vcare4u.authservice.config.JwtUtils;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthFilter = new JwtAuthFilter(jwtUtils, userDetailsService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";

        request.addHeader("Authorization", "Bearer " + token);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(jwtUtils.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtils.isTokenValid(token, username)).thenReturn(true);

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithNoAuthorizationHeader() throws ServletException, IOException {
        // No Authorization header set

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilter_WithValidToken() throws Exception {
        String token = "valid.jwt.token";
        String username = "testuser";

        request.addHeader("Authorization", "Bearer " + token);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(jwtUtils.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtils.isTokenValid(token, username)).thenReturn(true);

        jwtAuthFilter.doFilter(request, response, filterChain); // Use public method

        verify(filterChain).doFilter(request, response);
    }

}
