package com.vcare4u.labservice.service;

import com.vcare4u.labservice.model.LabPaymentRequest;

import java.util.List;

public interface LabPaymentRequestService {
    LabPaymentRequest createRequest(LabPaymentRequest request);
    List<LabPaymentRequest> getRequestsByPatient(Long patientId);
    LabPaymentRequest markAsPaid(Long appointmentId);
    LabPaymentRequest getByAppointmentId(Long appointmentId);
    List<LabPaymentRequest> getRequestsByDoctor(Long doctorId);
}
