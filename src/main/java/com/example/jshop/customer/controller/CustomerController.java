package com.example.jshop.customer.controller;

import com.example.jshop.customer.domain.AuthenticationDataDto;
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
    ResponseEntity<LoggedCustomerDto> addCustomer(@RequestBody LoggedCustomerDto loggedCustomerDto) {
        customerService.createNewCustomer(loggedCustomerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> removeCustomer(@RequestBody AuthenticationDataDto authenticationDataDto) throws UserNotFoundException, AccessDeniedException {
        customerService.removeCustomer(authenticationDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("my_orders")
    ResponseEntity<List<OrderDtoToCustomer>> showMyOrders(@RequestBody AuthenticationDataDto authenticationDataDto) throws UserNotFoundException, AccessDeniedException {
        return ResponseEntity.ok(customerService.showMyOrders(authenticationDataDto));
    }

    @PutMapping
    ResponseEntity<Void> cancelOrderLogged(@RequestParam Long orderId, @RequestBody AuthenticationDataDto authenticationDataDto) throws UserNotFoundException, AccessDeniedException, OrderNotFoundException {
        cartService.cancelOrderLogged(orderId, authenticationDataDto);
        return ResponseEntity.ok().build();
    }
}
