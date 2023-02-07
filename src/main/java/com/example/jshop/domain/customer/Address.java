package com.example.jshop.domain.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue
    Long addressId;

    @NonNull
    String street;
    @NonNull
    String houseNo;
    @NonNull
    String flatNo;
    @NonNull
    String zipCode;
    @NonNull
    String city;
    @NonNull
    String country;
}
