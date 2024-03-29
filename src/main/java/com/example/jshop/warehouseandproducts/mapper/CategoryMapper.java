package com.example.jshop.warehouseandproducts.mapper;

import java.util.List;
import com.example.jshop.warehouseandproducts.domain.category.Category;
import com.example.jshop.warehouseandproducts.domain.category.CategoryDto;
import com.example.jshop.warehouseandproducts.domain.category.CategoryWithProductsDto;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    @Autowired
    ProductMapper productMapper;

    public Category mapToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getName());
    }

    public List<CategoryWithProductsDto> mapToCategoryDtoListAllInfo(List<Category> categoryList) {
        return categoryList.stream()
            .map(category -> new CategoryWithProductsDto(category.getCategoryID(), category.getName(),
                productMapper.mapToProductDtoList(category.getListOfProducts()))).collect(toList());
    }

    public CategoryWithProductsDto mapToCategoryDtoAllInfo(Category category) {
        return new CategoryWithProductsDto(category.getCategoryID(), category.getName(),
            productMapper.mapToProductDtoList(category.getListOfProducts()));
    }
}
