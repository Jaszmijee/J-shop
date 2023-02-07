package com.example.jshop.domain.cart;

import com.example.jshop.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {

    @Id
    @GeneratedValue
    Long itemId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "products_product_id")
    Product product;

    int quantity;

    @ManyToOne
    @JoinColumn(name = "carts_cartID")
    Cart cart;

}

