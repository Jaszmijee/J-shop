package com.example.jshop.warehouseandproducts.domain.product;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDtoAllInfo {

    private Long productId;
    private String category;
    private String productName;
    private String description;
    private BigDecimal price;
}
