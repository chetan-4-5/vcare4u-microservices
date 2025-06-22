package com.vcare4u.appointmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.vcare4u.appointmentservice.dto.PatientDto;

@FeignClient(name = "patient-service")
public interface PatientClient {
    @GetMapping("/api/patients/{id}")
    PatientDto getPatientById(@PathVariable Long id, @RequestHeader("Authorization") String token);
}
