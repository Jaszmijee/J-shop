package com.example.jshop.warehouseandproducts.domain.warehouse;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WarehouseDto {

    private Long warehouseId;
    private Long productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private int quantity;
}
