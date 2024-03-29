package com.example.jshop.cartsandorders.service;

import java.math.BigDecimal;
import com.example.jshop.cartsandorders.domain.cart.Item;
import com.example.jshop.cartsandorders.repository.ItemRepository;
import com.example.jshop.warehouseandproducts.domain.category.Category;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import com.example.jshop.warehouseandproducts.repository.CategoryRepository;
import com.example.jshop.warehouseandproducts.repository.ProductRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Nested
    @DisplayName("test save")
    @Transactional
    class TestSave {

        @Test
        void savePositive() {
            //Given
            Category category = new Category("testCategory");
            categoryRepository.save(category);
            Product product = new Product("testProduct", "testDescription", category, BigDecimal.TEN);
            productRepository.save(product);
            Item item = Item.builder()
                .product(product)
                .quantity(20)
                .build();

            //When
            Item savedItem = itemRepository.save(item);
            Long itemId = savedItem.getItemId();

            //Then
            assertTrue(itemRepository.existsById(itemId));
            assertEquals(20, itemRepository.findById(itemId).get().getQuantity());
        }
    }

    @Nested
    @Transactional
    @DisplayName("test delete")
    class TestDelete {

        @Test
        void deletePositive() {
            //Given
            Category category = new Category("testCategory");
            categoryRepository.save(category);
            Product product = new Product("testProduct", "testDescription", category, BigDecimal.TEN);
            productRepository.save(product);
            Item item = Item.builder()
                .product(product)
                .quantity(20)
                .build();
            Item savedItem = itemRepository.save(item);
            Long itemId = savedItem.getItemId();

            //When
            itemRepository.delete(item);

            //Then
            assertFalse(itemRepository.existsById(itemId));
        }
    }
}
