package com.vcare4u.patientservice.config;

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
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // ADMIN only endpoints
                        .requestMatchers(HttpMethod.POST, "/api/patients").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").permitAll() //.hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients").hasRole("ADMIN") // ✅ Only ADMIN can get all patients

                        // Shared: Admin or patient can get by ID, controller will validate it

                        .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("ADMIN", "PATIENT","DOCTOR")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
