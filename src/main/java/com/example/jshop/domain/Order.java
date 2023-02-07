package com.example.jshop.domain;

import com.example.jshop.domain.cart.Cart;
import com.example.jshop.domain.customer.Customer;

import java.time.LocalDate;

public class Order {

    Long orderID;
    Customer customer;
    Cart cart;
    LocalDate created;
    LocalDate paid;
    ORDER_STATUS order_status;
}
