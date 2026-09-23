package com.vcare4u.appointmentservice.repository;

import com.vcare4u.appointmentservice.model.AppointmentSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    List<AppointmentSlot> findByDoctorIdAndIsBookedFalse(Long doctorId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AppointmentSlot> findByDoctorIdAndStartTimeAndIsBookedFalse(Long doctorId, LocalDateTime startTime);

}
