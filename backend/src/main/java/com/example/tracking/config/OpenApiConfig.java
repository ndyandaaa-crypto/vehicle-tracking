package com.example.tracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI trackingApi() {
        return new OpenAPI().info(new Info()
                .title("Vehicle Tracking API")
                .version("1.0.0")
                .description("View tracked vehicles, their routes and driver details. "
                        + "Data is stored in an in-memory H2 database and vehicle movement is simulated."));
    }
}
