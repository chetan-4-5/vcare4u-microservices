package com.vcare4u.appointmentservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String reason;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String notes;
    private String doctorName;
    private String department;
    private String labPaymentStatus;

}
