package com.example.jshop.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class OrderDtoToCustomer {

    Long orderID;
    LocalDate createdOn;
    String listOfProducts;
    String totalPrice;
    String status;
    LocalDate paymentDue;

    public void setPaymentDue(LocalDate paymentDue) {
        this.paymentDue = paymentDue;
    }
}
