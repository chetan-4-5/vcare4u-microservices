package com.vcare4u.patientservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @DeleteMapping("/api/auth/delete-user/{userId}")
    void deleteUser(@PathVariable Long userId, @RequestHeader("Authorization") String token);
}

