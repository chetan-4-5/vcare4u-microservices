package com.vcare4u.labservice.controller;

import com.vcare4u.labservice.config.JwtUtils;
import com.vcare4u.labservice.dto.LabPaymentRequestDTO;
import com.vcare4u.labservice.model.LabPaymentRequest;
import com.vcare4u.labservice.service.LabPaymentRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab/payment")

public class LabPaymentRequestController {

    @Autowired
    private LabPaymentRequestService service;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/request")
    public ResponseEntity<LabPaymentRequest> createRequest(@RequestBody LabPaymentRequestDTO dto, HttpServletRequest request) {
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        if (authUserId == null || !authUserId.equals(dto.getDoctorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LabPaymentRequest paymentRequest = new LabPaymentRequest();
        paymentRequest.setAppointmentId(dto.getAppointmentId());
        paymentRequest.setDoctorId(dto.getDoctorId());
        paymentRequest.setPatientId(dto.getPatientId());
        paymentRequest.setTestName(dto.getTestName());
        paymentRequest.setAmount(dto.getAmount());
        paymentRequest.setStatus(LabPaymentRequest.PaymentStatus.PENDING);


        return ResponseEntity.ok(service.createRequest(paymentRequest));
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabPaymentRequest>> getByPatient(@PathVariable Long patientId, HttpServletRequest request) {
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        String role = jwtUtils.extractRoleFromRequest(request);
        if (!"ADMIN".equals(role) && (authUserId == null || !patientId.equals(authUserId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.getRequestsByPatient(patientId));
    }

    @PutMapping("/mark-paid/{appointmentId}")
    public ResponseEntity<LabPaymentRequest> markAsPaid(@PathVariable Long appointmentId, HttpServletRequest request) {
        LabPaymentRequest paymentRequest = service.getByAppointmentId(appointmentId);
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        if (authUserId == null || !paymentRequest.getPatientId().equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.markAsPaid(appointmentId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<LabPaymentRequest> getByAppointment(@PathVariable Long appointmentId, HttpServletRequest request) {
        LabPaymentRequest paymentRequest = service.getByAppointmentId(appointmentId);
        String role = jwtUtils.extractRoleFromRequest(request);
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        if (!canAccessPayment(paymentRequest, role, authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(paymentRequest);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<LabPaymentRequest>> getByDoctor(@PathVariable Long doctorId, HttpServletRequest request) {
        Long authUserId = jwtUtils.extractUserIdFromRequest(request);
        String role = jwtUtils.extractRoleFromRequest(request);
        if (!"ADMIN".equals(role) && (authUserId == null || !doctorId.equals(authUserId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.getRequestsByDoctor(doctorId));
    }

    private boolean canAccessPayment(LabPaymentRequest paymentRequest, String role, Long authUserId) {
        return "ADMIN".equals(role)
                || ("PATIENT".equals(role) && paymentRequest.getPatientId().equals(authUserId))
                || ("DOCTOR".equals(role) && paymentRequest.getDoctorId().equals(authUserId));
    }

}
