package com.example.jshop.cartsandorders.domain.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ItemDto {

    private Long productId;
    private String productName;
    private int productQuantity;
    private BigDecimal calculatedPrice;
}
