package com.example.jshop.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoggedCustomerDto {

    String username;
    char[] password;
}
