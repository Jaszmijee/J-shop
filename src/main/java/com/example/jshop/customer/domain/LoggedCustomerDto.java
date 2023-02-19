package com.example.jshop.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoggedCustomerDto {

    String username;
    char[] password;
}
