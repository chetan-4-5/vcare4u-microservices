package com.vcare4u.appointmentservice.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI appointmentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Appointment Service API")
                        .version("1.0")
                        .description("API documentation for Appointment Service")
                        .contact(new Contact()
                                .name("Vcare4U Tech Team")
                                .email("support@vcare4u.com")
                                .url("https://vcare4u.com")
                        )
                );
    }
}
