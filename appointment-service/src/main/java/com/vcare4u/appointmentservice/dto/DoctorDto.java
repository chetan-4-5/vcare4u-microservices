package com.vcare4u.appointmentservice.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Long id;
    private String fullName;
    private String email;
    private String specialization;
    private String phone;
    private String qualification;
    private String department;
}
