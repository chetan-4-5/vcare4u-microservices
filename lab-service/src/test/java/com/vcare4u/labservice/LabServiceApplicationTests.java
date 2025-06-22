package com.vcare4u.labservice;

import com.vcare4u.labservice.dto.LabReportDto;
import com.vcare4u.labservice.dto.LabTestDto;
import com.vcare4u.labservice.exception.ResourceNotFoundException;
import com.vcare4u.labservice.service.LabReportService;
import com.vcare4u.labservice.service.LabTestService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class LabServiceApplicationTests {

    @Autowired
    private LabTestService labTestService;

    @Autowired
    private LabReportService labReportService;

    @Test
    void contextLoads() {
        // Ensure application context starts successfully
    }

    // ------------------ LabTestService Tests ------------------

    @Test
    void testAddAndGetLabTest_Positive() {
        LabTestDto dto = new LabTestDto();
        dto.setTestName("Thyroid");
        dto.setDescription("Thyroid Function Test");
        dto.setPrice(300.0);

        LabTestDto saved = labTestService.addTest(dto);
        assertNotNull(saved.getId());
        assertEquals("Thyroid", saved.getTestName());
    }

    @Test
    void testGetAllTests_NotEmpty() {
        List<LabTestDto> tests = labTestService.getAllTests();
        assertFalse(tests.isEmpty());
    }

    @Test
    void testGetLabTestById_Positive() {
        LabTestDto test = labTestService.getAllTests().get(0);
        LabTestDto result = labTestService.getTestById(test.getId());
        assertEquals(test.getTestName(), result.getTestName());
    }

    @Test
    void testGetLabTestById_Negative() {
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            labTestService.getTestById(-1L);
        });
        assertTrue(ex.getMessage().contains("LabTest"));
    }

    // ------------------ LabReportService Tests ------------------

    @Test
    void testGenerateAndGetLabReport_Positive() {
        LabReportDto dto = new LabReportDto();
        dto.setPatientId(1L);  // Make sure this patient exists or handled gracefully
        dto.setDoctorId(1L);   // Make sure this doctor exists or handled gracefully
        dto.setTestId(1L);
        dto.setResult("Normal");

        LabReportDto saved = labReportService.generateReport(dto);
        assertNotNull(saved.getId());
        assertEquals("Normal", saved.getResult());
    }

    @Test
    void testGetLabReportById_Positive() {
        LabReportDto report = labReportService.getReportsByDoctor(1L).stream().findFirst().orElse(null);
        if (report != null) {
            LabReportDto result = labReportService.getReportById(report.getId());
            assertEquals(report.getResult(), result.getResult());
        }
    }

    @Test
    void testGetLabReportById_Negative() {
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            labReportService.getReportById(-100L);
        });
        assertTrue(ex.getMessage().contains("LabReport"));
    }

    @Test
    void testGetReportsByNonExistingPatient() {
        List<LabReportDto> reports = labReportService.getReportsByPatient(-100L);
        assertTrue(reports.isEmpty());
    }

    @Test
    void testGetReportsByNonExistingDoctor() {
        List<LabReportDto> reports = labReportService.getReportsByDoctor(-200L);
        assertTrue(reports.isEmpty());
    }
}
