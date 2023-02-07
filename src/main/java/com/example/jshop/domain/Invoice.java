package com.example.jshop.domain;

import com.example.jshop.domain.product.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Invoice {

    Long invoiceID;
    String invoiceName;
    LocalDate created;
    LocalDate dateOfPayment;
    List<Product> listOfProducts;
    BigDecimal sum;

}
