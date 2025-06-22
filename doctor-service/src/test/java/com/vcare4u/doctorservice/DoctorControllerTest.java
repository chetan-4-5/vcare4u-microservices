package com.vcare4u.doctorservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcare4u.doctorservice.config.JwtAuthFilter;
import com.vcare4u.doctorservice.controller.DoctorController;
import com.vcare4u.doctorservice.dto.DoctorDto;
import com.vcare4u.doctorservice.feign.AuthServiceClient;
import com.vcare4u.doctorservice.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = DoctorController.class)

class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private AuthServiceClient authServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorDto doctorDto;

    @BeforeEach
    void setUp() {
        doctorDto = new DoctorDto(
                1L,
                "Dr. Jane",
                "jane@health.com",
                "Dermatology",
                "9999999999",
                "MD",
                "Skin"
        );
    }

    @Test
    void testCreateDoctor() throws Exception {
        Mockito.when(doctorService.createDoctor(any())).thenReturn(doctorDto);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Dr. Jane"))
                .andExpect(jsonPath("$.email").value("jane@health.com"));
    }

    @Test
    void testGetDoctorById() throws Exception {
        Mockito.when(doctorService.getDoctorById(1L)).thenReturn(doctorDto);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.specialization").value("Dermatology"));
    }

    @Test
    void testGetAllDoctors() throws Exception {
        Mockito.when(doctorService.getAllDoctors()).thenReturn(List.of(doctorDto));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Dr. Jane"));
    }

    @Test
    void testUpdateDoctor() throws Exception {
        DoctorDto updatedDto = new DoctorDto(
                1L, "Dr. Updated", "updated@doc.com", "ENT", "1234567890", "MBBS", "ENT");

        Mockito.when(doctorService.updateDoctor(Mockito.eq(1L), any())).thenReturn(updatedDto);

        mockMvc.perform(put("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Dr. Updated"))
                .andExpect(jsonPath("$.specialization").value("ENT"));
    }

    @Test
    void testDeleteDoctor() throws Exception {
        doNothing().when(doctorService).deleteDoctor(1L);
        doNothing().when(authServiceClient).deleteUser(1L);
        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());
    }
}
