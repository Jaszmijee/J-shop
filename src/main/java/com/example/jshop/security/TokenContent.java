package com.example.jshop.security;

import lombok.Data;

import java.util.List;

@Data
public class TokenContent {
    private List<String> groups;
}
