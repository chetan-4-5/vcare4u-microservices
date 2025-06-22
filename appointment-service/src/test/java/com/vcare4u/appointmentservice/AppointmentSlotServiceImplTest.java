package com.vcare4u.appointmentservice;

import com.vcare4u.appointmentservice.dto.AppointmentSlotDto;
import com.vcare4u.appointmentservice.model.AppointmentSlot;
import com.vcare4u.appointmentservice.repository.AppointmentSlotRepository;
import com.vcare4u.appointmentservice.service.impl.AppointmentSlotServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentSlotServiceImplTest {

    @InjectMocks
    private AppointmentSlotServiceImpl service;

    @Mock
    private AppointmentSlotRepository repository;

    private AppointmentSlot slot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slot = AppointmentSlot.builder()
                .id(1L)
                .doctorId(101L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusMinutes(30))
                .isBooked(false)
                .build();
    }

    @Test
    void testCreateSlot_Positive() {
        when(repository.save(any(AppointmentSlot.class))).thenReturn(slot);

        AppointmentSlotDto dto = new AppointmentSlotDto();
        dto.setDoctorId(101L);
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());

        AppointmentSlotDto saved = service.createSlot(dto);

        assertNotNull(saved);
        assertEquals(slot.getDoctorId(), saved.getDoctorId());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testGetAvailableSlots_Positive() {
        List<AppointmentSlot> slotList = List.of(slot);
        when(repository.findByDoctorIdAndIsBookedFalse(101L)).thenReturn(slotList);

        List<AppointmentSlotDto> result = service.getAvailableSlotsByDoctor(101L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isBooked());
        verify(repository, times(1)).findByDoctorIdAndIsBookedFalse(101L);
    }
}