package com.example.jshop.administrator;

import com.example.jshop.error_handlers.exceptions.CategoryExistsException;
import com.example.jshop.error_handlers.exceptions.InvalidArgumentException;
import com.example.jshop.warehouse_and_products.domain.category.Category;
import com.example.jshop.warehouse_and_products.repository.CategoryRepository;
import com.example.jshop.warehouse_and_products.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void addNewCategory() {
        CategoryService service = new CategoryService();

        Category category = new Category("al");
        assertThrows(InvalidArgumentException.class, () -> service.addCategory(category)) ;

    }
}