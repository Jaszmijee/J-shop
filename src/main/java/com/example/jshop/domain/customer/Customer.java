package com.example.jshop.domain.customer;

import com.example.jshop.domain.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    Long customerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carts_CartId")
    Cart cart;

    @Column(name = "name")
    @NonNull
    String firstName;

    @Column(name = "lastname")
    @NonNull
    String lastName;

    @Column(name = "e-mail")
    @NonNull
    String email;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_addresId")
    Address address;
}
