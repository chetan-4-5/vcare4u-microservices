package com.vcare4u.labservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Swagger / Docs Access
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // 📌 Lab Tests — ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/lab/tests").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/lab/tests/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/lab/tests").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/lab/tests/**").hasRole("ADMIN")

                        // 📌 Lab Reports — DOCTOR & PATIENT
                        .requestMatchers(HttpMethod.POST, "/api/lab/reports").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/lab/reports/doctor/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/lab/reports/patient/**").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/lab/reports/**").authenticated()

                        // ✅ Lab Payment Requests
                        .requestMatchers(HttpMethod.POST, "/api/lab/payment/request").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/lab/payment/mark-paid/**").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/lab/payment/patient/**").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/lab/payment/appointment/**").hasAnyRole("DOCTOR", "PATIENT")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
