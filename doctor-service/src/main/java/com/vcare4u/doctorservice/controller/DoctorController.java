package com.vcare4u.doctorservice.controller;


import com.vcare4u.doctorservice.config.JwtUtils;
import com.vcare4u.doctorservice.dto.DoctorDto;
import com.vcare4u.doctorservice.feign.AuthServiceClient;

import com.vcare4u.doctorservice.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final AuthServiceClient authServiceClient;
    private final JwtUtils jwtUtils;


    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto dto) {
        return ResponseEntity.ok(doctorService.createDoctor(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @RequestBody DoctorDto dto, HttpServletRequest request) {
        String role = jwtUtils.extractRoleFromRequest(request);
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        if (!"ADMIN".equals(role) && (!"DOCTOR".equals(role) || !id.equals(authUserId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id, HttpServletRequest request) {
        doctorService.deleteDoctor(id);
        authServiceClient.deleteUser(id, request.getHeader("Authorization"));
        return ResponseEntity.noContent().build();
    }

}
