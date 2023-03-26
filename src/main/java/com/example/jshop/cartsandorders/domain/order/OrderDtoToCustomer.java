package com.example.jshop.cartsandorders.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OrderDtoToCustomer {

    private Long orderId;
    private LocalDate createdOn;
    private String listOfProducts;
    private String totalPrice;
    private String status;
    private LocalDate paymentDue;
}
