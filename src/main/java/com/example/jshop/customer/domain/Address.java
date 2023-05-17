package com.example.jshop.customer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long addressId;

    @NonNull
    private String street;
    @NonNull
    private String houseNo;
    @NonNull
    private String flatNo;
    @NonNull
    private String zipCode;
    @NonNull
    private String city;
    @NonNull
    private String country;

    @OneToOne
    @JoinColumn(name = "logged_customers_customerID")
    private LoggedCustomer _Logged_Customer;

    public Address(@NonNull String street, @NonNull String houseNo, @NonNull String flatNo, @NonNull String zipCode,
        @NonNull String city, @NonNull String country) {
        this.street = street;
        this.houseNo = houseNo;
        this.flatNo = flatNo;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }
}
