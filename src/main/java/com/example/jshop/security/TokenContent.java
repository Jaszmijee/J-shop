package com.example.jshop.security;

import java.util.List;
import lombok.Data;

@Data
public class TokenContent {

    private List<String> member_of_group;
}
