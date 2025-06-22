package com.vcare4u.labservice.repository;

import com.vcare4u.labservice.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
}
