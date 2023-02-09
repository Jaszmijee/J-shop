package com.example.jshop.domain;

import com.example.jshop.domain.cart.Cart;
import com.example.jshop.domain.cart.CartStatus;
import com.example.jshop.domain.cart.ItemDto;
import com.example.jshop.domain.customer.Customer_Logged;
import com.example.jshop.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    public void setPaid(LocalDate paid) {
        this.paid = paid;
    }

    public void setOrder_status(ORDER_STATUS order_status) {
        this.order_status = order_status;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

