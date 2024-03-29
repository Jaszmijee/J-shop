/*
package com.example.jshop.cartsandorders.service;

import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.cart.CartStatus;
import com.example.jshop.cartsandorders.domain.cart.Item;
import com.example.jshop.cartsandorders.domain.order.OrderStatus;
import com.example.jshop.cartsandorders.domain.order.Order;
import com.example.jshop.cartsandorders.repository.CartRepository;
import com.example.jshop.cartsandorders.repository.ItemRepository;
import com.example.jshop.cartsandorders.repository.OrderRepository;
import com.example.jshop.customer.domain.Address;
import com.example.jshop.customer.domain.LoggedCustomer;
import com.example.jshop.customer.repository.CustomerRepository;
import com.example.jshop.errorhandlers.exceptions.InvalidOrderStatusException;
import com.example.jshop.errorhandlers.exceptions.OrderNotFoundException;
import com.example.jshop.warehouseandproducts.domain.category.Category;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import com.example.jshop.warehouseandproducts.domain.warehouse.Warehouse;
import com.example.jshop.warehouseandproducts.repository.CategoryRepository;
import com.example.jshop.warehouseandproducts.repository.ProductRepository;
import com.example.jshop.warehouseandproducts.repository.WarehouseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Nested
    @Transactional
    @DisplayName("test save")
    class TestSave {
        @Test
        void savePositive() {
            //Given
            Order order = new Order();

            //When
            Order savedOrder = orderService.save(order);
            Long orderId = savedOrder.getOrderID();

            //Then
            assertTrue(orderRepository.existsById(orderId));
        }
    }

    @Nested
    @Transactional
    @DisplayName("test findOrderById")
    class TestFindOrderById {
        @Test
        void findOrderByIdOrderNotFoundException() {
            //Given
            Order order = new Order();
            Order savedOrder = orderRepository.save(order);
            Long orderId = savedOrder.getOrderID();

            //When & Then
            assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(orderId + 1));
        }

        @Test
        void findOrderByIdPositive() {
            //Given
            Order order = new Order();
            Order savedOrder = orderRepository.save(order);
            Long orderId = savedOrder.getOrderID();

            //When & Then
            try {
                Order searchedOrder = orderService.findOrderById(orderId);
                assertNotNull(searchedOrder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @Transactional
    @DisplayName("test findByIdAndUserName")
    class TestFindByIdAndUserName {
        @Test
        void findByIdAndUserNameOrderNotFoundException() {
            //Given
            Category category = new Category("testCategory");
            categoryRepository.save(category);
            Product product = new Product("testName", "testDescription", category, new BigDecimal("5.00"));
            category.getListOfProducts().add(product);
            categoryRepository.save(category);
            productRepository.save(product);
            Warehouse warehouse = new Warehouse(product, 20);
            warehouseRepository.save(warehouse);
            Cart cart = Cart.builder()
                    .cartStatus(CartStatus.FINALIZED)
                    .listOfItems(new ArrayList<>())
                    .build();
            cartRepository.save(cart);
            Item item = Item.builder()
                    .product(product)
                    .quantity(10)
                    .cart(cart)
                    .build();
            itemRepository.save(item);
            cart.getListOfItems().add(item);
            cartRepository.save(cart);
            String pwwd = "password";
            LoggedCustomer loggedCustomer = new LoggedCustomer("user", pwwd.toCharArray(), "Adam", "DDD", "ptr@ptr",
                    new Address("Fairy", "5", "5", "55-555", "Maputo", "Mozambique"));
            customerRepository.save(loggedCustomer);
            Order order = new Order(loggedCustomer, cart, LocalDate.of(2023, 2, 25), OrderStatus.PAID, "dummyList", new
                    BigDecimal("25.00"));
            orderRepository.save(order);

            //When & Then
            assertThrows(OrderNotFoundException.class, () -> orderService.findByIdAndUserName(order.getOrderID() + 1, "user"));
            assertThrows(OrderNotFoundException.class, () -> orderService.findByIdAndUserName(order.getOrderID(), "anotherUser"));
            assertDoesNotThrow(() -> orderService.findByIdAndUserName(order.getOrderID(), "user"));
        }

        @Test
        void findByIdAndUserNamePositive() {
            Category category = new Category("testCategory");
            categoryRepository.save(category);
            Product product = new Product("testName", "testDescription", category, new BigDecimal("5.00"));
            category.getListOfProducts().add(product);
            categoryRepository.save(category);
            productRepository.save(product);
            Warehouse warehouse = new Warehouse(product, 20);
            warehouseRepository.save(warehouse);
            Cart cart = Cart.builder()
                    .cartStatus(CartStatus.FINALIZED)
                    .listOfItems(new ArrayList<>())
                    .build();
            cartRepository.save(cart);
            Item item = Item.builder()
                    .product(product)
                    .quantity(10)
                    .cart(cart)
                    .build();
            itemRepository.save(item);
            cart.getListOfItems().add(item);
            cartRepository.save(cart);
            String pwwd = "password";
            LoggedCustomer loggedCustomer = new LoggedCustomer("user", pwwd.toCharArray(), "Adam", "DDD", "ptr@ptr",
                    new Address("Fairy", "5", "5", "55-555", "Maputo", "Mozambique"));
            customerRepository.save(loggedCustomer);
            Order order = new Order(loggedCustomer, cart, LocalDate.of(2023, 2, 25), OrderStatus.PAID, "dummyList", new
                    BigDecimal("25.00"));
            orderRepository.save(order);

            //When & Then
            try {
                Order searchedOrder = orderService.findByIdAndUserName(order.getOrderID(), "user");
                assertEquals("dummyList", searchedOrder.getListOfProducts());
                assertEquals("PAID", searchedOrder.getOrderStatus().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @Transactional
    @DisplayName("test findOrders")
    class TestFindOrders {
        @Test
        void findOrdersInvalidOrderStatusException() {
            //Given
            Order order1 = Order.builder()
                    .orderStatus(OrderStatus.PAID)
                    .build();
            String status2 = "";
            String status3 = "bla";
            orderRepository.save(order1);

            //When & Then
            assertThrows(InvalidOrderStatusException.class, () -> orderService.findOrders(status2));
            assertThrows(InvalidOrderStatusException.class, () -> orderService.findOrders(status3));
        }

        @Test
        void findOrdersPositive() {
            //Given
            Order order1 = Order.builder()
                    .orderStatus(OrderStatus.PAID)
                    .build();
            orderRepository.save(order1);
            try {
                System.out.println(orderService.findOrderById(order1.getOrderID()).getOrderStatus().toString());
            } catch (OrderNotFoundException e) {
                throw new RuntimeException(e);
            }
            //When & Then
            try {
                assertEquals(1, orderService.findOrders("paid").size());
                assertEquals(0, orderService.findOrders("unpaid").size());
                assertEquals(1, orderService.findOrders(null).size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @Transactional
    @DisplayName("test findOrdersOfCustomer")
    class TestFindOrdersOfCustomer {
        @Test
        void findOrdersOfCustomerEmptyList() {
            //Given
            String pwwd = "password";
            LoggedCustomer loggedCustomer = new LoggedCustomer("user", pwwd.toCharArray(), "Adam", "DDD", "ptr@ptr",
                    new Address("Fairy", "5", "5", "55-555", "Maputo", "Mozambique"));
            customerRepository.save(loggedCustomer);

            //When & Then
            try {
                assertEquals(0, orderService.findOrdersOfCustomer("user").size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @Transactional
    @DisplayName("test deleteOrder")
    class TestDeleteOrder {
        @Test
        void deleteOrderPositive() {
            //Given
            Order order = new Order();
            Order savedOrder = orderService.save(order);
            Long orderId = savedOrder.getOrderID();

            //When
            orderService.deleteOrder(order);

            //Then
            assertFalse(orderRepository.existsById(orderId));
        }
    }
}*/
