package com.vcare4u.labservice.feign;

import com.vcare4u.labservice.payload.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @GetMapping("/api/patients/{id}")
    PatientResponse getPatientById(@PathVariable Long id);
}
