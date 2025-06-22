package com.vcare4u.appointmentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/appointments/*/status").hasRole("DOCTOR")

                    // Admin-only
                .requestMatchers(HttpMethod.GET, "/api/appointments").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/appointments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/appointments").hasAnyRole("ADMIN", "PATIENT")

                // Patient-only
                .requestMatchers(HttpMethod.GET, "/api/appointments/my").hasRole("PATIENT")

                
                

                // Doctor-only
                .requestMatchers(HttpMethod.GET, "/api/appointments/doctor/**").hasRole("DOCTOR")

                // Shared: allow both ADMIN and PATIENT to view appointment by ID
                .requestMatchers(HttpMethod.GET, "/api/appointments/{id}").hasAnyRole("ADMIN", "PATIENT","DOCTOR")

                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
