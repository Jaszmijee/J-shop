package com.example.jshop.domain.cart;

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
