package com.vcare4u.appointmentservice.repository;

import com.vcare4u.appointmentservice.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    List<AppointmentSlot> findByDoctorIdAndIsBookedFalse(Long doctorId);
    Optional<AppointmentSlot> findByDoctorIdAndStartTime(Long doctorId, LocalDateTime startTime);

}
