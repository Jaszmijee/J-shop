/*
package com.example.jshop.domain.customer;

import com.example.jshop.domain.order.Order;
import com.example.jshop.domain.cart.Cart;
import com.example.jshop.domain.product.Product;
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

    @OneToMany(targetEntity = Order.class,
            mappedBy = "customer",
            fetch = FetchType.LAZY)
    List<Order> listOfOrders = new ArrayList<>();
}
*/
