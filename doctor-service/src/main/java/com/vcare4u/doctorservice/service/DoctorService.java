package com.vcare4u.doctorservice.service;

import com.vcare4u.doctorservice.dto.DoctorDto;

import java.util.List;

public interface DoctorService {

    DoctorDto createDoctor(DoctorDto dto);

    DoctorDto getDoctorById(Long id);

    List<DoctorDto> getAllDoctors();

    DoctorDto updateDoctor(Long id, DoctorDto dto);

    void deleteDoctor(Long id);
}
