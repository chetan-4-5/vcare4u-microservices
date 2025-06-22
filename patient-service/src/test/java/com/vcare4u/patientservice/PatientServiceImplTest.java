package com.vcare4u.patientservice;

import com.vcare4u.patientservice.dto.PatientDto;
import com.vcare4u.patientservice.exception.ResourceNotFoundException;
import com.vcare4u.patientservice.model.Patient;
import com.vcare4u.patientservice.repository.PatientRepository;
import com.vcare4u.patientservice.service.impl.PatientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private PatientRepository repository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient samplePatient;
    private PatientDto sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePatient = new Patient();
        samplePatient.setId(1L);
        samplePatient.setFullName("John Doe");
        samplePatient.setEmail("john@example.com");

        sampleDto = new PatientDto();
        sampleDto.setId(1L);
        sampleDto.setFullName("John Doe");
        sampleDto.setEmail("john@example.com");
    }

    @Test
    void testCreatePatient() {
        when(repository.save(any(Patient.class))).thenReturn(samplePatient);

        PatientDto created = patientService.createPatient(sampleDto);

        assertNotNull(created);
        assertEquals("John Doe", created.getFullName());
        verify(repository, times(1)).save(any(Patient.class));
    }

    @Test
    void testGetPatientById_ValidId() {
        when(repository.findById(1L)).thenReturn(Optional.of(samplePatient));

        PatientDto result = patientService.getPatientById(1L);

        assertEquals("John Doe", result.getFullName());
        verify(repository).findById(1L);
    }

    @Test
    void testGetPatientById_InvalidId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientById(1L));

        assertTrue(exception.getMessage().contains("Patient"));
    }

    @Test
    void testGetAllPatients() {
        when(repository.findAll()).thenReturn(Arrays.asList(samplePatient));

        List<PatientDto> list = patientService.getAllPatients();

        assertEquals(1, list.size());
        assertEquals("John Doe", list.get(0).getFullName());
    }

    @Test
    void testUpdatePatient_ValidId() {
        when(repository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(repository.save(any(Patient.class))).thenReturn(samplePatient);

        sampleDto.setFullName("Updated Name");

        PatientDto updated = patientService.updatePatient(1L, sampleDto);

        assertEquals("Updated Name", updated.getFullName());
        verify(repository).save(any(Patient.class));
    }

    @Test
    void testUpdatePatient_InvalidId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.updatePatient(1L, sampleDto));
    }

    @Test
    void testDeletePatient_ValidId() {
        when(repository.findById(1L)).thenReturn(Optional.of(samplePatient));
        doNothing().when(repository).delete(samplePatient);

        patientService.deletePatient(1L);

        verify(repository).delete(samplePatient);
    }

    @Test
    void testDeletePatient_InvalidId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.deletePatient(1L));
    }
}
