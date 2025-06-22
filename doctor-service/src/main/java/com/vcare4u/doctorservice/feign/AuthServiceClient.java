package com.vcare4u.doctorservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @DeleteMapping("/api/auth/delete-user/{userId}")
    void deleteUser(@PathVariable Long userId);
}

