package com.vcare4u.authservice;

import com.vcare4u.authservice.config.JwtUtils;
import com.vcare4u.authservice.dto.AuthRequest;
import com.vcare4u.authservice.dto.AuthResponse;
import com.vcare4u.authservice.dto.RegisterRequest;
import com.vcare4u.authservice.model.Role;
import com.vcare4u.authservice.model.User;
import com.vcare4u.authservice.repository.UserRepository;
import com.vcare4u.authservice.service.impl.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("greeshma");
        request.setEmail("greeshma@example.com");
        request.setPassword("pass123");
        request.setRole(Role.PATIENT);

        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("User registered successfully as PATIENT", response.getMessage());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin() {
        AuthRequest request = new AuthRequest();
        request.setUsername("greeshma");
        request.setPassword("pass123");

        User user = User.builder()
                .id(1L)
                .username("greeshma")
                .password("encodedPass")
                .role(Role.PATIENT)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("greeshma", "pass123"));

        when(userRepository.findByUsername("greeshma")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken("greeshma", "PATIENT",(long) 1)).thenReturn("mocked-jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
    }


    @Test
    void testLoginThrowsExceptionForInvalidUser() {
        AuthRequest request = new AuthRequest();
        request.setUsername("unknown");
        request.setPassword("pass");

        // Simulate authentication is successful (even if user doesn't exist later)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken("unknown", "pass"));

        // Simulate user not found in DB
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Assert that RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(request);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
