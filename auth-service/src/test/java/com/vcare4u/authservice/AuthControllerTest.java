package com.vcare4u.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcare4u.authservice.controller.AuthController;
import com.vcare4u.authservice.dto.AuthRequest;
import com.vcare4u.authservice.dto.AuthResponse;
import com.vcare4u.authservice.dto.RegisterRequest;
import com.vcare4u.authservice.model.Role;
import com.vcare4u.authservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)

public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private AuthResponse registerResponse;
    private AuthResponse loginResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("greeshma");
        registerRequest.setEmail("greeshma@example.com");
        registerRequest.setPassword("securePass");
        registerRequest.setRole(Role.PATIENT);

        authRequest = new AuthRequest();
        authRequest.setUsername("greeshma");
        authRequest.setPassword("securePass");

        registerResponse = new AuthResponse(null, "User registered successfully as PATIENT");
        loginResponse = new AuthResponse("dummy-jwt-token", "Login successful");
    }

    @Test
    void testRegister() throws Exception {
        when(authService.register(any(RegisterRequest.class))).thenReturn(registerResponse);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print()) // helpful for debugging
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully as PATIENT"));
    }

    @Test
    void testLogin() throws Exception {
        when(authService.login(any(AuthRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void testJwtTestEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("JWT is valid. You are authenticated."));
    }
}
