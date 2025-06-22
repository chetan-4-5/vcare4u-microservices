package com.vcare4u.labservice.service.impl;

import com.vcare4u.labservice.dto.LabTestDto;
import com.vcare4u.labservice.exception.ResourceNotFoundException;
import com.vcare4u.labservice.model.LabTest;
import com.vcare4u.labservice.repository.LabTestRepository;
import com.vcare4u.labservice.service.LabTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;

    @Override
    public LabTestDto addTest(LabTestDto dto) {
        LabTest test = new LabTest();
        BeanUtils.copyProperties(dto, test);
        return mapToDto(labTestRepository.save(test));
    }

    @Override
    public List<LabTestDto> getAllTests() {
        return labTestRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabTestDto getTestById(Long id) {
        LabTest test = labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LabTest", "id", id));
        return mapToDto(test);
    }

    @Override
    public void deleteTest(Long id) {
        labTestRepository.deleteById(id);
    }

    private LabTestDto mapToDto(LabTest test) {
        LabTestDto dto = new LabTestDto();
        BeanUtils.copyProperties(test, dto);
        return dto;
    }
}


