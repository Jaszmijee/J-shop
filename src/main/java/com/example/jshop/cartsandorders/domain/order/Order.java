package com.example.jshop.cartsandorders.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.customer.domain.LoggedCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long orderID;

    @ManyToOne
    @JoinColumn(name = "logged_customers_customerID")
    private LoggedCustomer loggedCustomer;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "paid")
    private LocalDate paid;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "items", columnDefinition = "TEXT")
    private String listOfProducts;

    @Column(name = "calculatedValue")
    private BigDecimal calculatedPrice;

    @Column(name = "paymentDue")
    private LocalDate paymentDue;

    @OneToOne
    @JoinColumn(name = "carts_cart_id")
    private Cart cart;

    @Column(name = "camunda_process_Id")
    private String camundaProcessId;

    public Order(LoggedCustomer loggedCustomer, LocalDate created, OrderStatus orderStatus, String listOfProducts,
        BigDecimal calculatedPrice, Cart cart, String camundaProcessId) {
        this.loggedCustomer = loggedCustomer;
        this.created = created;
        this.orderStatus = orderStatus;
        this.listOfProducts = listOfProducts;
        this.calculatedPrice = calculatedPrice;
        this.cart = cart;
        this.camundaProcessId = camundaProcessId;
    }

    public void setPaid(LocalDate paid) {
        this.paid = paid;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setLoggedCustomer(LoggedCustomer loggedCustomer) {
        this.loggedCustomer = loggedCustomer;
    }

    public void setPaymentDue(LocalDate paymentDue) {
        this.paymentDue = paymentDue;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setListOfProducts(String listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public void setCamundaProcessId(String camundaProcessId) {
        this.camundaProcessId = camundaProcessId;
    }
}

