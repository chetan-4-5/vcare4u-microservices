package com.vcare4u.labservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabPaymentRequestDTO {
    private Long appointmentId;
    private Long patientId;
    private String testName;
    private Double amount;
    private Long doctorId;
}
