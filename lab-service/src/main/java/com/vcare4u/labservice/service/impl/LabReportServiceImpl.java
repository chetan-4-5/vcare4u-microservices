package com.vcare4u.labservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.vcare4u.labservice.model.LabTest;
import com.vcare4u.labservice.repository.LabTestRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.vcare4u.labservice.dto.LabReportDto;
import com.vcare4u.labservice.exception.ResourceNotFoundException;
import com.vcare4u.labservice.feign.DoctorClient;
import com.vcare4u.labservice.feign.PatientClient;
import com.vcare4u.labservice.model.LabReport;
import com.vcare4u.labservice.repository.LabReportRepository;
import com.vcare4u.labservice.service.LabReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LabReportServiceImpl implements LabReportService {

    private final LabReportRepository labReportRepository;
    private final LabTestRepository labTestRepository;
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;

    @Override
    public LabReportDto generateReport(LabReportDto dto) {
        // ✅ Fetch the LabTest entity using testId
        LabTest labTest = labTestRepository.findById(dto.getTestId())
                .orElseThrow(() -> new RuntimeException("Invalid Test ID"));

        // ✅ Manually construct LabReport
        LabReport report = LabReport.builder()
                .patientId(dto.getPatientId())
                .doctorId(dto.getDoctorId())
                .appointmentId(dto.getAppointmentId())
                .result(dto.getResult())
                .reportDate(dto.getReportDate())
                .test(labTest) // associate the actual test
                .build();

        // ✅ Save and convert back to DTO
        LabReport saved = labReportRepository.save(report);
        return mapToDto(saved);
    }



    @Override
    public List<LabReportDto> getReportsByPatient(Long patientId) {
        return labReportRepository.findByPatientId(patientId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LabReportDto> getReportsByDoctor(Long doctorId) {
        return labReportRepository.findByDoctorId(doctorId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public LabReportDto getReportById(Long id) {
        LabReport report = labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LabReport", "id", id));
        return mapToDto(report);
    }

    private LabReportDto mapToDto(LabReport report) {
        LabReportDto dto = new LabReportDto();
        BeanUtils.copyProperties(report, dto);
        dto.setTestId(report.getTest() != null ? report.getTest().getId() : null);

        try {
            dto.setDoctorName(doctorClient.getDoctorById(report.getDoctorId()).getFullName());
        } catch (Exception e) {
            dto.setDoctorName("Doctor not available");
        }

        try {
            dto.setPatientName(patientClient.getPatientById(report.getPatientId()).getFullName());
        } catch (Exception e) {
            dto.setPatientName("Patient not available");
        }

        return dto;
    }

}

