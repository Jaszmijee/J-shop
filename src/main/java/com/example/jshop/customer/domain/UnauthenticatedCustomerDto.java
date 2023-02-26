package com.example.jshop.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UnauthenticatedCustomerDto {

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
