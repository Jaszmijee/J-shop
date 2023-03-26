package com.example.jshop.cartsandorders.domain.cart;

import com.example.jshop.warehouseandproducts.domain.product.Product;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {

    @Id
    @GeneratedValue
    private Long itemId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "products_product_id")
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "carts_cartID")
    private Cart cart;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

