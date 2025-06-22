package com.vcare4u.labservice.service;

import com.vcare4u.labservice.dto.LabReportDto;

import java.util.List;

public interface LabReportService {
    LabReportDto generateReport(LabReportDto dto);
    List<LabReportDto> getReportsByPatient(Long patientId);
    List<LabReportDto> getReportsByDoctor(Long doctorId);
    LabReportDto getReportById(Long id);
}

