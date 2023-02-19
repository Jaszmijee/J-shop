package com.example.jshop.customer.controller;

import com.example.jshop.customer.domain.CustomerDto;
import com.example.jshop.customer.domain.LoggedCustomerDto;
import com.example.jshop.carts_and_orders.domain.order.OrderDtoToCustomer;
import com.example.jshop.error_handlers.exceptions.AccessDeniedException;
import com.example.jshop.error_handlers.exceptions.OrderNotFoundException;
import com.example.jshop.error_handlers.exceptions.UserNotFoundException;
import com.example.jshop.carts_and_orders.service.CartService;
import com.example.jshop.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CartService cartService;

    @PostMapping
    ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.createNewCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> removeCustomer(@RequestBody LoggedCustomerDto loggedCustomerDto) throws UserNotFoundException, AccessDeniedException {
        customerService.removeCustomer(loggedCustomerDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("my_orders")
    ResponseEntity<List<OrderDtoToCustomer>> showMyOrders(@RequestBody LoggedCustomerDto loggedCustomerDto) throws UserNotFoundException, AccessDeniedException {
        return ResponseEntity.ok(customerService.showMyOrders(loggedCustomerDto));
    }

    @PutMapping
    ResponseEntity<Void> cancelOrderLogged(@RequestParam Long orderId, @RequestBody LoggedCustomerDto loggedCustomerDto) throws UserNotFoundException, AccessDeniedException, OrderNotFoundException {
        cartService.cancelOrderLogged(orderId, loggedCustomerDto);
        return ResponseEntity.ok().build();
    }
}
