package com.vcare4u.labservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabReportDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long testId;
    private String result;
    private LocalDateTime reportDate;
    private Long appointmentId;

    // Added fields from other services
    private String doctorName;
    private String patientName;
}
