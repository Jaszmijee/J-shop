package com.example.jshop.warehouse_and_products.service;

import com.example.jshop.error_handlers.exceptions.ProductNotFoundException;
import com.example.jshop.warehouse_and_products.domain.product.Product;
import com.example.jshop.warehouse_and_products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Nested
    @Transactional
    @DisplayName("test saveProduct")
    class TestSaveProduct {
    @Test
    void saveProduct() {
        //Given
        Product product = new Product();

        //When
        Product savedProduct = productService.saveProduct(product);
        Long productId = savedProduct.getProductID();

        // & Then
        assertTrue(productRepository.existsById(productId));
    }
    }

    @Nested
    @Transactional
    @DisplayName("test findProductById")
    class TestFindProductById {
    @Test
    void findProductByIdProductNotFoundException() {
        //Given
        Product product = new Product();
        Product savedProduct = productService.saveProduct(product);
        Long productId = savedProduct.getProductID();

        //When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId+ 1));
    }

        @Test
        void findProductByIdPositive() {
            //Given
            Product product = new Product();
            Product savedProduct = productService.saveProduct(product);
            Long productId = savedProduct.getProductID();

            //When & Then
            assertDoesNotThrow(() -> productService.findProductById(productId));
        }
    }

    @Test
    void deleteProductById() {
    }

    @Test
    void findAllProducts() {
    }
}