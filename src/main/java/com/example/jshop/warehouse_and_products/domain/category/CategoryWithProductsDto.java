package com.example.jshop.warehouse_and_products.domain.category;

import com.example.jshop.warehouse_and_products.domain.product.ProductDtoAllInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryWithProductsDto {

    Long categoryId;
    String categoryName;
    List<ProductDtoAllInfo> listOfProducts;
}

