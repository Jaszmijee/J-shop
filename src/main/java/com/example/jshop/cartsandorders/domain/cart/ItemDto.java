package com.example.jshop.cartsandorders.domain.cart;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemDto {

    private Long productId;
    private String productName;
    private int productQuantity;
    private BigDecimal calculatedPrice;
}
