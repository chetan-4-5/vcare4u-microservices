package com.vcare4u.appointmentservice.feign;

import com.vcare4u.appointmentservice.dto.LabPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "lab-service")
public interface LabPaymentClient {
    @GetMapping("/api/lab/payment/appointment/{appointmentId}")
    LabPaymentResponse getByAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token
    );
}
