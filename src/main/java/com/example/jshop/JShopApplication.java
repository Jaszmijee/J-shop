package com.example.jshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class JShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JShopApplication.class, args);
    }
}
