package com.vcare4u.appointmentservice.service;

import com.vcare4u.appointmentservice.dto.AppointmentSlotDto;

import java.util.List;
public interface AppointmentSlotService {
    AppointmentSlotDto createSlot(AppointmentSlotDto dto);
    List<AppointmentSlotDto> getAvailableSlotsByDoctor(Long doctorId);
}
