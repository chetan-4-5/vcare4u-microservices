package com.vcare4u.doctorservice;

import com.vcare4u.doctorservice.dto.DoctorDto;
import com.vcare4u.doctorservice.exception.ResourceNotFoundException;
import com.vcare4u.doctorservice.model.Doctor;
import com.vcare4u.doctorservice.repository.DoctorRepository;
import com.vcare4u.doctorservice.service.impl.DoctorServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorDto doctorDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = Doctor.builder()
                .id(1L)
                .fullName("Dr. John")
                .email("john@example.com")
                .specialization("Cardiology")
                .phone("9876543210")
                .qualification("MBBS")
                .department("Cardiology")
                .build();

        doctorDto = new DoctorDto(
                1L,
                "Dr. John",
                "john@example.com",
                "Cardiology",
                "9876543210",
                "MBBS",
                "Cardiology"
        );
    }

    @Test
    void testCreateDoctor() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorDto result = doctorService.createDoctor(doctorDto);

        assertEquals(doctorDto.getFullName(), result.getFullName());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testGetDoctorById_Found() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorDto result = doctorService.getDoctorById(1L);

        assertEquals(doctorDto.getEmail(), result.getEmail());
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDoctorById_NotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(1L));
    }

    @Test
    void testGetAllDoctors() {
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(doctor));

        List<DoctorDto> result = doctorService.getAllDoctors();

        assertEquals(1, result.size());
        assertEquals("Dr. John", result.get(0).getFullName());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateDoctor_Success() {
        DoctorDto updatedDto = new DoctorDto(
                1L, "Dr. Updated", "updated@care.com", "Neurology", "1234567890", "MD", "Neuro");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorDto result = doctorService.updateDoctor(1L, updatedDto);

        assertEquals("Dr. Updated", result.getFullName());
        assertEquals("Neurology", result.getSpecialization());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void testUpdateDoctor_NotFound() {
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateDoctor(2L, doctorDto));
    }

    @Test
    void testDeleteDoctor_Success() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(1L);

        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void testDeleteDoctor_NotFound() {
        when(doctorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.deleteDoctor(3L));
    }
}
