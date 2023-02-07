package com.example.jshop.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {

    private String productName;
    private String description;
    private String categoryName;
    private BigDecimal price;

}
