package com.vcare4u.labservice.service.impl;

import com.vcare4u.labservice.model.LabPaymentRequest;
import com.vcare4u.labservice.repository.LabPaymentRequestRepository;
import com.vcare4u.labservice.service.LabPaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabPaymentRequestServiceImpl implements LabPaymentRequestService {

    @Autowired
    private LabPaymentRequestRepository repo;

    @Override
    public LabPaymentRequest createRequest(LabPaymentRequest request) {
        request.setStatus(LabPaymentRequest.PaymentStatus.PENDING);
        return repo.save(request);
    }

    @Override
    public List<LabPaymentRequest> getRequestsByPatient(Long patientId) {
        return repo.findByPatientId(patientId);
    }

    @Override
    public LabPaymentRequest markAsPaid(Long appointmentId) {
        LabPaymentRequest request = repo.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("No request found"));
        request.setStatus(LabPaymentRequest.PaymentStatus.PAID);
        return repo.save(request);
    }

    @Override
    public LabPaymentRequest getByAppointmentId(Long appointmentId) {
        return repo.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("No request found"));
    }
    @Override
    public List<LabPaymentRequest> getRequestsByDoctor(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }
}
