package com.vcare4u.appointmentservice.service.impl;

import com.vcare4u.appointmentservice.dto.AppointmentSlotDto;
import com.vcare4u.appointmentservice.model.AppointmentSlot;
import com.vcare4u.appointmentservice.repository.AppointmentSlotRepository;
import com.vcare4u.appointmentservice.service.AppointmentSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final AppointmentSlotRepository slotRepository;

    @Override
    public AppointmentSlotDto createSlot(AppointmentSlotDto dto) {
        AppointmentSlot slot = new AppointmentSlot();
        BeanUtils.copyProperties(dto, slot);
        return mapToDto(slotRepository.save(slot));
    }

    @Override
    public List<AppointmentSlotDto> getAvailableSlotsByDoctor(Long doctorId) {
        List<AppointmentSlot> availableSlots = slotRepository.findByDoctorIdAndIsBookedFalse(doctorId);

        return availableSlots.stream()
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime())) // Optional: Sort by time
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    private AppointmentSlotDto mapToDto(AppointmentSlot slot) {
        AppointmentSlotDto dto = new AppointmentSlotDto();
        BeanUtils.copyProperties(slot, dto);
        return dto;
    }
}
