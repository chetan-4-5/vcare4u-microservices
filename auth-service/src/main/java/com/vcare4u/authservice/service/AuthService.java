package com.vcare4u.authservice.service;

import com.vcare4u.authservice.dto.AuthRequest;
import com.vcare4u.authservice.dto.AuthResponse;
import com.vcare4u.authservice.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    void deleteUserById(Long userId);
}