package com.vcare4u.patientservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PatientDto {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String email;
    private String phone;
    private String gender;
    private String address;
    private String medicalHistory;
}