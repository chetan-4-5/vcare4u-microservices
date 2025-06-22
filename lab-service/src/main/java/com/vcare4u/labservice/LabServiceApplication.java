package com.vcare4u.labservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.vcare4u.labservice.feign")
public class LabServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabServiceApplication.class, args);
    }
}
