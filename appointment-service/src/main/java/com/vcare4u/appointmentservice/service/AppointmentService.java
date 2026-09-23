package com.vcare4u.appointmentservice.service;

import com.vcare4u.appointmentservice.dto.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    default AppointmentDto createAppointment(AppointmentDto dto) {
        return createAppointment(dto, null);
    }

    AppointmentDto createAppointment(AppointmentDto dto, String token);
    AppointmentDto getAppointmentById(Long id, String token);

    default List<AppointmentDto> getAllAppointments() {
        return getAllAppointments(null);
    }

    List<AppointmentDto> getAllAppointments(String token);

    default AppointmentDto updateAppointment(Long id, AppointmentDto dto) {
        return updateAppointment(id, dto, null);
    }

    AppointmentDto updateAppointment(Long id, AppointmentDto dto, String token);
    void deleteAppointment(Long id);

    default List<AppointmentDto> getAppointmentsByPatient(Long patientId) {
        return getAppointmentsByPatient(patientId, null);
    }

    List<AppointmentDto> getAppointmentsByPatient(Long patientId, String token);

    default List<AppointmentDto> getAppointmentsByDoctor(Long doctorId) {
        return getAppointmentsByDoctor(doctorId, null);
    }

    List<AppointmentDto> getAppointmentsByDoctor(Long doctorId, String token);

    default AppointmentDto updateAppointmentStatus(Long id, String status) {
        return updateAppointmentStatus(id, status, null);
    }

    AppointmentDto updateAppointmentStatus(Long id, String status, String token);

    
}
