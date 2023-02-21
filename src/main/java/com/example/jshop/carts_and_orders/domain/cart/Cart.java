package com.example.jshop.carts_and_orders.domain.cart;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "carts")
public class Cart {

    @Id
    @GeneratedValue
    Long cartID;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    CartStatus cartStatus;

    @Column(name = "items_cart")
    @OneToMany(targetEntity = Item.class,
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    List<Item> listOfItems = new ArrayList<>();

    @Column(name = "total")
    BigDecimal calculatedPrice;

    @Column(name = "created")
    LocalDate created;


    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }
}
