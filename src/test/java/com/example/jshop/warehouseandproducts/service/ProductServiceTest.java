package com.example.jshop.warehouseandproducts.service;

import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import com.example.jshop.warehouseandproducts.repository.ProductRepository;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Test
    void deleteProductById() {
    }

    @Test
    void findAllProducts() {
    }

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
            assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId + 1));
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
}
