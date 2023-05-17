package com.example.jshop.customer.controller;

import java.util.List;
import com.example.jshop.cartsandorders.domain.order.OrderDtoToCustomer;
import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.customer.domain.AuthenticationDataDto;
import com.example.jshop.customer.domain.LoggedCustomerDto;
import com.example.jshop.customer.service.CustomerService;
import com.example.jshop.errorhandlers.exceptions.AccessDeniedException;
import com.example.jshop.errorhandlers.exceptions.InvalidCustomerDataException;
import com.example.jshop.errorhandlers.exceptions.OrderNotFoundException;
import com.example.jshop.errorhandlers.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/j-shop/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CartService cartService;

    @PostMapping
    ResponseEntity<Void> createNewCustomer(@RequestBody LoggedCustomerDto loggedCustomerDto)
        throws InvalidCustomerDataException {
        customerService.createNewCustomer(loggedCustomerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> removeCustomer(@RequestBody AuthenticationDataDto authenticationDataDto)
        throws UserNotFoundException, AccessDeniedException, InvalidCustomerDataException {
        customerService.removeCustomer(authenticationDataDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("show-my-orders")
    ResponseEntity<List<OrderDtoToCustomer>> showMyOrders(Authentication auth)
        throws UserNotFoundException, AccessDeniedException, InvalidCustomerDataException {
        String userName = (String) auth.getPrincipal();
        return ResponseEntity.ok(customerService.showMyOrders(userName));
    }

    @DeleteMapping("delete-my-order")
    ResponseEntity<Void> cancelOrderLogged(@RequestParam Long orderId, Authentication auth)
        throws UserNotFoundException, OrderNotFoundException, InvalidCustomerDataException {
        String userName = (String) auth.getPrincipal();
        cartService.cancelOrderLogged(orderId, userName);
        return ResponseEntity.ok().build();
    }
}
