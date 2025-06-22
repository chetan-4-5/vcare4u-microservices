package com.vcare4u.labservice.controller;

import com.vcare4u.labservice.dto.LabPaymentRequestDTO;
import com.vcare4u.labservice.model.LabPaymentRequest;
import com.vcare4u.labservice.service.LabPaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab/payment")

public class LabPaymentRequestController {

    @Autowired
    private LabPaymentRequestService service;

    @PostMapping("/request")
    public LabPaymentRequest createRequest(@RequestBody LabPaymentRequestDTO dto) {
        LabPaymentRequest request = new LabPaymentRequest();
        request.setAppointmentId(dto.getAppointmentId());
        request.setDoctorId(dto.getDoctorId());
        request.setPatientId(dto.getPatientId());
        request.setTestName(dto.getTestName());
        request.setAmount(dto.getAmount());
        request.setStatus(LabPaymentRequest.PaymentStatus.PENDING);


        return service.createRequest(request);
    }


    @GetMapping("/patient/{patientId}")
    public List<LabPaymentRequest> getByPatient(@PathVariable Long patientId) {
        return service.getRequestsByPatient(patientId);
    }

    @PutMapping("/mark-paid/{appointmentId}")
    public LabPaymentRequest markAsPaid(@PathVariable Long appointmentId) {
        return service.markAsPaid(appointmentId);
    }

    @GetMapping("/appointment/{appointmentId}")
    public LabPaymentRequest getByAppointment(@PathVariable Long appointmentId) {
        return service.getByAppointmentId(appointmentId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<LabPaymentRequest> getByDoctor(@PathVariable Long doctorId) {
        return service.getRequestsByDoctor(doctorId);
    }

}
