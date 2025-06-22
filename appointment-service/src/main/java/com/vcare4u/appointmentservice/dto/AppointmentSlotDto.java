package com.vcare4u.appointmentservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentSlotDto {
    private Long id;
    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;
}
