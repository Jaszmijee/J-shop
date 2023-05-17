package com.example.jshop.customer.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "logged_customers")
public class LoggedCustomer {

    @OneToMany(targetEntity = Order.class,
        mappedBy = "loggedCustomer",
        fetch = FetchType.LAZY)
    List<Order> listOfOrders = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long customerID;
    @Column(name = "userName", unique = true)
    private String userName;
    @Column(name = "password")
    private char[] password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carts_CartId")
    private Cart cart;
    @Column(name = "name")
    @NonNull
    private String firstName;
    @Column(name = "lastname")
    @NonNull
    private String lastName;
    @Column(name = "email")
    @NonNull
    private String email;
    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_addresId")
    private Address address;

    public LoggedCustomer(String userName, char[] password, @NonNull String firstName, @NonNull String lastName,
        @NonNull String email, @NonNull Address address) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}

