package com.vcare4u.authservice.service.impl;

import com.vcare4u.authservice.dto.AuthRequest;
import com.vcare4u.authservice.dto.AuthResponse;
import com.vcare4u.authservice.dto.RegisterRequest;
import com.vcare4u.authservice.model.User;
import com.vcare4u.authservice.repository.UserRepository;
import com.vcare4u.authservice.service.AuthService;
import com.vcare4u.authservice.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name(), user.getId());
        // No token returned here
        return new AuthResponse(token, "User registered successfully as " + request.getRole().name());
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        // Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Fetch user from DB to get the role
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate token with role
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name(),user.getId());

        return new AuthResponse(token, "Login successful");
    }
    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

}
