package com.vcare4u.labservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcare4u.labservice.dto.LabReportDto;
import com.vcare4u.labservice.dto.LabTestDto;
import com.vcare4u.labservice.service.LabReportService;
import com.vcare4u.labservice.service.LabTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lab")
@RequiredArgsConstructor
public class LabController {

    private final LabTestService labTestService;
    private final LabReportService labReportService;

    // Lab test management
    @PostMapping("/tests")
    public ResponseEntity<LabTestDto> addTest(@RequestBody LabTestDto dto) {
        return ResponseEntity.ok(labTestService.addTest(dto));
    }

    @GetMapping("/tests")
    public ResponseEntity<List<LabTestDto>> getAllTests() {
        return ResponseEntity.ok(labTestService.getAllTests());
    }

    @GetMapping("/tests/{id}")
    public ResponseEntity<LabTestDto> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(labTestService.getTestById(id));
    }

    @DeleteMapping("/tests/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        labTestService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    // Lab report management
    @PostMapping("/reports")
    public ResponseEntity<LabReportDto> generateReport(@RequestBody LabReportDto dto) {
        return ResponseEntity.ok(labReportService.generateReport(dto));
    }

    @GetMapping("/reports/patient/{patientId}")
    public ResponseEntity<List<LabReportDto>> getReportsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labReportService.getReportsByPatient(patientId));
    }

    @GetMapping("/reports/doctor/{doctorId}")
    public ResponseEntity<List<LabReportDto>> getReportsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(labReportService.getReportsByDoctor(doctorId));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<LabReportDto> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(labReportService.getReportById(id));
    }
    
}
