package com.example.jshop.cartsandorders.domain.order;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

