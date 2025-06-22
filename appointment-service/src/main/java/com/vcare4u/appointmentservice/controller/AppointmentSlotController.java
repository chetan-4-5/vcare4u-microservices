package com.vcare4u.appointmentservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcare4u.appointmentservice.dto.AppointmentSlotDto;
import com.vcare4u.appointmentservice.service.AppointmentSlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class AppointmentSlotController {

    private final AppointmentSlotService slotService;

    @PostMapping
    public ResponseEntity<AppointmentSlotDto> createSlot(@RequestBody AppointmentSlotDto dto) {
        return ResponseEntity.ok(slotService.createSlot(dto));
    }

    @GetMapping("/available/{doctorId}")
    public ResponseEntity<List<AppointmentSlotDto>> getAvailableSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(slotService.getAvailableSlotsByDoctor(doctorId));
    }
}
