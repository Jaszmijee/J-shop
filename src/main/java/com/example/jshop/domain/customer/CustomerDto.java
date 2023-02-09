package com.example.jshop.domain.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CustomerDto {

    String userName;
    String password;
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
