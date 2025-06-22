package com.vcare4u.appointmentservice.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vcare4u.appointmentservice.dto.AppointmentDto;
import com.vcare4u.appointmentservice.service.AppointmentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDto dto) {

        if (dto.getAppointmentDateTime() == null || dto.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Cannot book an appointment in the past.");
        }

        AppointmentDto savedAppointment = appointmentService.createAppointment(dto);
        return ResponseEntity.ok(savedAppointment);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return ResponseEntity.ok(appointmentService.getAppointmentById(id, token));
    }


    // ADMIN only
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // PATIENT
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentDto>> getPatientAppointments(@RequestParam Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // DOCTOR
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    // DOCTOR
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        System.out.println("In Update Status Appointments");
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, body.get("status")));
    }

    // ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    // ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
