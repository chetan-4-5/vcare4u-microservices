package com.vcare4u.patientservice.service.impl;

import com.vcare4u.patientservice.dto.PatientDto;
import com.vcare4u.patientservice.exception.ResourceNotFoundException;
import com.vcare4u.patientservice.model.Patient;
import com.vcare4u.patientservice.repository.PatientRepository;
import com.vcare4u.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    @Override
    public PatientDto createPatient(PatientDto dto) {
        Patient patient = new Patient();
        BeanUtils.copyProperties(dto, patient);
        return mapToDto(repository.save(patient));
    }

    @Override
    public PatientDto getPatientById(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        return mapToDto(patient);
    }

    @Override
    public List<PatientDto> getAllPatients() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDto updatePatient(Long id, PatientDto dto) {
        Patient existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setGender(dto.getGender());
        existing.setAddress(dto.getAddress());
        existing.setMedicalHistory(dto.getMedicalHistory());
        return mapToDto(repository.save(existing));
    }

    @Override
    public void deletePatient(Long id) {
        Patient existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        repository.delete(existing);
    }

    private PatientDto mapToDto(Patient patient) {
        PatientDto dto = new PatientDto();
        BeanUtils.copyProperties(patient, dto);
        return dto;
    }
}
