package com.example.jshop.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableProcessApplication("cart_service")
@ConditionalOnProperty(name = "camunda.bpm.enabled", havingValue = "true", matchIfMissing = true)
public class CamundaApplicationConfig {
}
