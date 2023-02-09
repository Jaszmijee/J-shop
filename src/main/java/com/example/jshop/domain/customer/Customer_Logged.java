package com.example.jshop.domain.customer;

import com.example.jshop.domain.Order;
import com.example.jshop.domain.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "logged_customers")
public class Customer_Logged {

    @Id
    @GeneratedValue
    Long customerID;

    @Column(name = "userName", unique = true)
    String userName;

    @Column(name = "password")
    char[] password;

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

    @OneToMany(targetEntity = Order.class,
            mappedBy = "customer",
            fetch = FetchType.LAZY)
    List<Order> listOfOrders = new ArrayList<>();

    public Customer_Logged(String userName, char[] password, @NonNull String firstName, @NonNull String lastName, @NonNull String email, @NonNull Address address) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
}

