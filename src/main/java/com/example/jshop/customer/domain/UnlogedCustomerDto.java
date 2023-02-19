package com.example.jshop.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnlogedCustomerDto {

    String firstName;
    String lastName;
    String email;

    String street;
    String houseNo;
    String flatNo;
    String zipCode;
    String city;
    String country;
}
