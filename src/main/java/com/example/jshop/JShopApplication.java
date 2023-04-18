package com.example.jshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@OpenAPIDefinition
@SpringBootApplication
@EnableScheduling
@EnableWebSecurity
public class JShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JShopApplication.class, args);
    }
}
