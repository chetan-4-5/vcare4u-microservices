package com.vcare4u.appointmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.vcare4u.appointmentservice.dto.DoctorDto;

@FeignClient(name = "doctor-service")
public interface DoctorClient {
    @GetMapping("/api/doctors/{id}")
    DoctorDto getDoctorById(@PathVariable Long id, @RequestHeader("Authorization") String token);
}

