package com.vcare4u.appointmentservice;

import com.vcare4u.appointmentservice.feign.DoctorClient;
import com.vcare4u.appointmentservice.feign.PatientClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

// Use classes = AppointmentServiceApplication.class to prevent component scan leaks
@SpringBootTest(classes = AppointmentServiceApplication.class)
class AppointmentServiceApplicationTests {

    @MockBean
    private DoctorClient doctorClient;

    @MockBean
    private PatientClient patientClient;

    @Test
    void contextLoads() {
    }
}
