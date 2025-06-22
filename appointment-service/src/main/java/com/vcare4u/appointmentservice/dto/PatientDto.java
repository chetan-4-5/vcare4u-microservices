package com.vcare4u.appointmentservice.dto;

import lombok.Data;

@Data
public class PatientDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private String medicalHistory;
}
