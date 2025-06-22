package com.vcare4u.labservice.repository;

import com.vcare4u.labservice.model.LabPaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabPaymentRequestRepository extends JpaRepository<LabPaymentRequest, Long> {
    List<LabPaymentRequest> findByPatientId(Long patientId);
    Optional<LabPaymentRequest> findByAppointmentId(Long appointmentId);
    List<LabPaymentRequest> findByDoctorId(Long doctorId);
}
