package com.example.jshop.domain.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ItemDto {

    Long productId;
    String productName;
    int productQuantity;
    BigDecimal calculatedPrice;
}
