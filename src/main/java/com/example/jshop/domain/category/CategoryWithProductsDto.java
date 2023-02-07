package com.example.jshop.domain.category;

import com.example.jshop.domain.product.ProductDto;
import com.example.jshop.domain.product.ProductDtoAllInfo;
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

