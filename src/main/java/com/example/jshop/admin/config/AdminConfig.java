package com.example.jshop.admin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AdminConfig {

    @Value("${apikey}")
    private String adminKey;

    @Value("${token}")
    private String adminToken;
}

