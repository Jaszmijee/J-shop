package com.example.jshop.cartsandorders.domain.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemsDto {

    private Long productId;
    private int quantity;
}
