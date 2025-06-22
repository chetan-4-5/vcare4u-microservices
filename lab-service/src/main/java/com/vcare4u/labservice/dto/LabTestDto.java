package com.vcare4u.labservice.dto;

import lombok.Data;

@Data
public class LabTestDto {
    private Long id;
    private String testName;
    private String description;
    private Double price;
}
