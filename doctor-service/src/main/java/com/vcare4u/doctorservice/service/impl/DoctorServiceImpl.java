package com.vcare4u.doctorservice.service.impl;

import com.vcare4u.doctorservice.dto.DoctorDto;
import com.vcare4u.doctorservice.exception.ResourceNotFoundException;
import com.vcare4u.doctorservice.model.Doctor;
import com.vcare4u.doctorservice.repository.DoctorRepository;
import com.vcare4u.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorDto createDoctor(DoctorDto dto) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(dto, doctor);
        return mapToDto(doctorRepository.save(doctor));
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return mapToDto(doctor);
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDto updateDoctor(Long id, DoctorDto dto) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setSpecialization(dto.getSpecialization());
        existing.setPhone(dto.getPhone());
        existing.setQualification(dto.getQualification());
        existing.setDepartment(dto.getDepartment());

        return mapToDto(doctorRepository.save(existing));
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        doctorRepository.delete(existing);
    }

    private DoctorDto mapToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        BeanUtils.copyProperties(doctor, dto);
        return dto;
    }
}