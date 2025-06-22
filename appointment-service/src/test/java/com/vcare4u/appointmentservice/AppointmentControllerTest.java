package com.vcare4u.appointmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcare4u.appointmentservice.config.JwtAuthFilter;
import com.vcare4u.appointmentservice.config.JwtUtils;
import com.vcare4u.appointmentservice.controller.AppointmentController;
import com.vcare4u.appointmentservice.dto.AppointmentDto;
import com.vcare4u.appointmentservice.service.AppointmentService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false) // disable Jwt filter for testing
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtAuthFilter jwtAuthFilter; // optional

    @MockBean
    private AppointmentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentDto sample;

    @BeforeEach
    void setup() {
        sample = new AppointmentDto();
        sample.setId(1L);
        sample.setDoctorId(201L);
        sample.setPatientId(101L);
        sample.setReason("Checkup");
        sample.setStatus("Scheduled");
        sample.setAppointmentDateTime(LocalDateTime.now());
    }

    @Test
    void createAppointment_ShouldReturnOk() throws Exception {
        when(service.createAppointment(any())).thenReturn(sample);

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(201));
    }

    @Test
    void getAppointmentById_ShouldReturnAppointment() throws Exception {
        when(service.getAppointmentById(eq(1L), anyString())).thenReturn(sample);

        mockMvc.perform(get("/api/appointments/1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllAppointments_ShouldReturnList() throws Exception {
        when(service.getAllAppointments()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(201));
    }
}