package com.vcare4u.appointmentservice.service;

import com.vcare4u.appointmentservice.dto.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto dto);
    AppointmentDto getAppointmentById(Long id, String token);

    List<AppointmentDto> getAllAppointments();
    AppointmentDto updateAppointment(Long id, AppointmentDto dto);
    void deleteAppointment(Long id);
    List<AppointmentDto> getAppointmentsByPatient(Long patientId);
    List<AppointmentDto> getAppointmentsByDoctor(Long doctorId);
    AppointmentDto updateAppointmentStatus(Long id, String status);

    
}
