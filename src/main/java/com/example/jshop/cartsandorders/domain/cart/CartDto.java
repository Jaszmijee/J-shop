package com.example.jshop.cartsandorders.domain.cart;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartID;
    private List<ItemDto> listOfProducts;
    private CartStatus cartStatus;
    private BigDecimal calculatedPrice;
    private Long discount;
    private BigDecimal finalPrice;
}
