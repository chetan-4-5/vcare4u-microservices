package com.vcare4u.appointmentservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.vcare4u.appointmentservice.model.AppointmentSlot;
import com.vcare4u.appointmentservice.repository.AppointmentSlotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vcare4u.appointmentservice.dto.AppointmentDto;
import com.vcare4u.appointmentservice.dto.DoctorDto;
import com.vcare4u.appointmentservice.dto.PatientDto;
import com.vcare4u.appointmentservice.exception.ResourceNotFoundException;
import com.vcare4u.appointmentservice.feign.DoctorClient;
import com.vcare4u.appointmentservice.feign.PatientClient;
import com.vcare4u.appointmentservice.model.Appointment;
import com.vcare4u.appointmentservice.repository.AppointmentRepository;
import com.vcare4u.appointmentservice.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository slotRepository;
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;
    private final RestTemplate restTemplate;

    private final String LAB_URL = "http://localhost:8080/lab-service/api/lab/payment/appointment/";

    @Override
    public List<AppointmentDto> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToDtoWithLabStatus)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDtoWithLabStatus)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
        appointment.setStatus(status);
        return mapToDtoWithLabStatus(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentDto createAppointment(AppointmentDto dto) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(dto, appointment);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentSlot slot = slotRepository.findByDoctorIdAndStartTime(
                dto.getDoctorId(), dto.getAppointmentDateTime()
        ).orElseThrow(() -> new RuntimeException("Matching slot not found"));

        slot.setBooked(true);
        slotRepository.save(slot);

        return mapToDtoWithLabStatus(savedAppointment);
    }

    @Override
    public AppointmentDto getAppointmentById(Long id, String token) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        AppointmentDto dto = mapToDtoWithLabStatus(appointment);

        try {
            DoctorDto doctor = doctorClient.getDoctorById(dto.getDoctorId(), token);
            PatientDto patient = patientClient.getPatientById(dto.getPatientId(), token);
            dto.setNotes("Doctor: " + doctor.getFullName() + " with Patient: " + patient.getFullName() + " Confirmed");
        } catch (Exception e) {
            dto.setNotes("Doctor or Patient details not available");
        }

        return dto;
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDtoWithLabStatus)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto updateAppointment(Long id, AppointmentDto dto) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        existing.setPatientId(dto.getPatientId());
        existing.setDoctorId(dto.getDoctorId());
        existing.setReason(dto.getReason());
        existing.setAppointmentDateTime(dto.getAppointmentDateTime());
        existing.setStatus(dto.getStatus());
        existing.setNotes(dto.getNotes());

        return mapToDtoWithLabStatus(appointmentRepository.save(existing));
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
        appointmentRepository.delete(appointment);
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        BeanUtils.copyProperties(appointment, dto);
        return dto;
    }

    private AppointmentDto mapToDtoWithLabStatus(Appointment appointment) {
        AppointmentDto dto = mapToDto(appointment);

        try {
            var response = restTemplate.getForObject(
                    LAB_URL + appointment.getId(),
                    LabPaymentResponse.class
            );
            dto.setLabPaymentStatus(response != null ? response.getStatus() : "NONE");
        } catch (Exception e) {
            dto.setLabPaymentStatus("NONE");
        }

        return dto;
    }

    // Inner DTO for lab-service response
    private static class LabPaymentResponse {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
