package com.example.jshop.domain.cart;

import java.math.BigDecimal;
import java.util.List;

public class CartDto {

    Long cartID;
    List<OrderedProducts> listOfProducts;
    CartStatus cartStatus;
    BigDecimal calculatedPrice;

   }
