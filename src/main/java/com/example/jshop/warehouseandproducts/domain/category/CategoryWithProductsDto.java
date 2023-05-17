package com.example.jshop.warehouseandproducts.domain.category;

import java.util.List;
import com.example.jshop.warehouseandproducts.domain.product.ProductDtoAllInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryWithProductsDto {

    private Long categoryId;
    private String categoryName;
    private List<ProductDtoAllInfo> listOfProducts;
}

