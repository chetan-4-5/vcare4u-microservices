package com.vcare4u.labservice.service;

import com.vcare4u.labservice.dto.LabTestDto;

import java.util.List;

public interface LabTestService {
    LabTestDto addTest(LabTestDto dto);
    List<LabTestDto> getAllTests();
    LabTestDto getTestById(Long id);
    void deleteTest(Long id);
}
