package com.example.jshop.domain.cart;

import com.example.jshop.domain.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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


    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public void setListOfItems(List<Item> listOfItems) {
        this.listOfItems = listOfItems;
    }
}
