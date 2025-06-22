package com.vcare4u.appointmentservice;

import com.vcare4u.appointmentservice.config.JwtAuthFilter;
import com.vcare4u.appointmentservice.config.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtUtils jwtUtils;
    private JwtAuthFilter jwtAuthFilter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        jwtAuthFilter = new JwtAuthFilter(jwtUtils);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext(); // ✅ Clear context before each test
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // ✅ Clear context after each test
    }

    @Test
    void shouldSetAuthenticationIfTokenIsValid() throws ServletException, IOException {
        String token = "valid.jwt.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.isTokenValid(token)).thenReturn(true);
        when(jwtUtils.extractUsername(token)).thenReturn("john");
        when(jwtUtils.extractRole(token)).thenReturn("USER");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("john");
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting(Object::toString).contains("ROLE_USER");

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipAuthenticationIfTokenIsInvalid() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.isTokenValid(token)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipAuthenticationIfNoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
