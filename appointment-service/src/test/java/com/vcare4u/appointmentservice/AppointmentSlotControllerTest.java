package com.vcare4u.appointmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcare4u.appointmentservice.controller.AppointmentSlotController;
import com.vcare4u.appointmentservice.dto.AppointmentSlotDto;
import com.vcare4u.appointmentservice.service.AppointmentSlotService;
import com.vcare4u.appointmentservice.config.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentSlotController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;  // Needed if controller depends on it

    @MockBean
    private AppointmentSlotService service;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentSlotDto slot;

    @BeforeEach
    void setUp() {
        slot = new AppointmentSlotDto();
        slot.setId(1L);
        slot.setDoctorId(101L);
        slot.setStartTime(LocalDateTime.now().plusDays(1));
        slot.setEndTime(LocalDateTime.now().plusDays(1).plusMinutes(30));
        slot.setBooked(false);
    }

    @Test
    void testCreateSlot_Positive() throws Exception {
        when(service.createSlot(any(AppointmentSlotDto.class))).thenReturn(slot);

        mockMvc.perform(post("/api/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetAvailableSlots_Positive() throws Exception {
        when(service.getAvailableSlotsByDoctor(101L)).thenReturn(List.of(slot));

        mockMvc.perform(get("/api/slots/available/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
