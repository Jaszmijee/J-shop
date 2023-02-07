/*
package com.example.jshop.domain.customer;

import com.example.jshop.domain.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customers")
public class Customer_Logged extends Customer {

    @Id
    @GeneratedValue
    Long customerID;

    @Column(name = "name", unique = true)
    String userName;

    @Column(name = "password")
    String password;

    @OneToOne
    @JoinColumn(referencedColumnName = "carts_cartID")
    Cart cart;
}
*/
