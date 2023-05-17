package com.example.jshop.warehouseandproducts.domain.product;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDto {

    private String productName;
    private String description;
    private String categoryName;
    private BigDecimal price;

}
