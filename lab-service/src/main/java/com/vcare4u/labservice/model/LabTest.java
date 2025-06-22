package com.vcare4u.labservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;

    private String description;

    private Double price;
}

