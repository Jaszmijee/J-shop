package com.example.jshop.customer.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
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

    @OneToOne
    @JoinColumn(name = "logged_customers_customerID")
    LoggedCustomer _Logged_Customer;

    public Address(@NonNull String street, @NonNull String houseNo, @NonNull String flatNo, @NonNull String zipCode, @NonNull String city, @NonNull String country) {
        this.street = street;
        this.houseNo = houseNo;
        this.flatNo = flatNo;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }
}
