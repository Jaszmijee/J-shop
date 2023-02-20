package com.example.jshop.carts_and_orders.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class OrderDtoToCustomer {

    Long orderId;
    LocalDate createdOn;
    String listOfProducts;
    String totalPrice;
    String status;
    LocalDate paymentDue;

    public void setPaymentDue(LocalDate paymentDue) {
        this.paymentDue = paymentDue;
    }
}
