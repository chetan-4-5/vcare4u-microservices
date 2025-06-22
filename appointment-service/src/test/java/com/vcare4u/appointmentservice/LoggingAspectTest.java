package com.vcare4u.appointmentservice;

import com.vcare4u.appointmentservice.service.AppointmentService;
import com.vcare4u.appointmentservice.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class LoggingAspectTest {

    @Autowired
    private AppointmentService appointmentService;

    @Test
    void testLoggingAspectLogsMethodEntryAndExit(CapturedOutput output) {
        appointmentService.getAllAppointments(); // call real method

        assertThat(output).contains("Entering method:");
        assertThat(output).contains("Exiting method:");
    }
}
