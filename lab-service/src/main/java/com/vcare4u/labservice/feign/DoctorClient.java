package com.vcare4u.labservice.feign;

import com.vcare4u.labservice.payload.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @GetMapping("/api/doctors/{id}")
    DoctorResponse getDoctorById(@PathVariable Long id);
}

