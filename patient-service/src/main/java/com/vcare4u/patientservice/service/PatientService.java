package com.vcare4u.patientservice.service;

import com.vcare4u.patientservice.dto.PatientDto;

import java.util.List;

public interface PatientService {
    PatientDto createPatient(PatientDto dto);
    PatientDto getPatientById(Long id);
    List<PatientDto> getAllPatients();
    PatientDto updatePatient(Long id, PatientDto dto);
    void deletePatient(Long id);
}
