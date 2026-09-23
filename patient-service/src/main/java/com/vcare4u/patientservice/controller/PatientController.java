package com.vcare4u.patientservice.controller;

import com.vcare4u.patientservice.config.JwtUtils;
import com.vcare4u.patientservice.dto.PatientDto;
import com.vcare4u.patientservice.feign.AuthServiceClient;
import com.vcare4u.patientservice.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final JwtUtils jwtUtils;
    private final AuthServiceClient authServiceClient;


    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto dto, HttpServletRequest request) {
        String role = jwtUtils.extractRoleFromRequest(request);
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);

        if ("PATIENT".equals(role)) {
            dto.setId(authUserId);
        } else if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(patientService.createPatient(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id, HttpServletRequest request) {
        String role = jwtUtils.extractRoleFromRequest(request);
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);

        PatientDto dto = patientService.getPatientById(id);

        if ("ADMIN".equals(role) || "DOCTOR".equals(role) || ("PATIENT".equals(role) && id.equals(authUserId))) {
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }



    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients(HttpServletRequest request) {
        String role = jwtUtils.extractRoleFromRequest(request);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ✅ prevent PATIENT
        }

        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @RequestBody PatientDto dto, HttpServletRequest request) {
        String role = jwtUtils.extractRoleFromRequest(request);
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        if (!"ADMIN".equals(role) && (!"PATIENT".equals(role) || !id.equals(authUserId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id, HttpServletRequest request) {
        patientService.deletePatient(id);
        authServiceClient.deleteUser(id, request.getHeader("Authorization"));
        return ResponseEntity.noContent().build();
    }
}
