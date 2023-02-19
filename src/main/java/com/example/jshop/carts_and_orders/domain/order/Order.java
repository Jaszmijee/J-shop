package com.example.jshop.carts_and_orders.domain.order;

import com.example.jshop.carts_and_orders.domain.cart.Cart;
import com.example.jshop.customer.domain.Customer_Logged;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    Long orderID;

    @ManyToOne
    @JoinColumn(name = "logged_customers_customerID")
    Customer_Logged customer;

    @Column(name = "Created")
    LocalDate created;

    @Column(name = "Paid")
    LocalDate paid;

    @Enumerated(value = EnumType.STRING)
    ORDER_STATUS order_status;

    @Column(name = "items", columnDefinition="TEXT")
    String listOfProducts;

    @Column(name = "value")
    BigDecimal calculatedPrice;

    @OneToOne
    @JoinColumn(name = "carts_cart_id")
    Cart cart;

    public Order(Customer_Logged customer, Cart cart, LocalDate created, ORDER_STATUS order_status, String listOfProducts, BigDecimal calculatedPrice) {
        this.customer = customer;
        this.created = created;
        this.order_status = order_status;
        this.listOfProducts = listOfProducts;
        this.calculatedPrice = calculatedPrice;
        this.cart = cart;
    }

    public Order(Cart cart, LocalDate created, ORDER_STATUS order_status, String listOfProducts, BigDecimal calculatedPrice) {
        this.created = created;
        this.order_status = order_status;
        this.listOfProducts = listOfProducts;
        this.calculatedPrice = calculatedPrice;
        this.cart = cart;
    }

    public void setPaid(LocalDate paid) {
        this.paid = paid;
    }

    public void setOrder_status(ORDER_STATUS order_status) {
        this.order_status = order_status;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setCustomer(Customer_Logged customer) {
        this.customer = customer;
    }
}

