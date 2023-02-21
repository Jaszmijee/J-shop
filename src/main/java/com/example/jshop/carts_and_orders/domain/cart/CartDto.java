package com.example.jshop.carts_and_orders.domain.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@AllArgsConstructor
public class CartDto {

    Long cartID;
    List<ItemDto> listOfProducts;
    CartStatus cartStatus;
    BigDecimal calculatedPrice;
   }
