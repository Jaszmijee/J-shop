package com.example.jshop.carts_and_orders.controller;

import com.example.jshop.customer.domain.UnlogedCustomerDto;
import com.example.jshop.carts_and_orders.domain.order.OrderDtoToCustomer;
import com.example.jshop.carts_and_orders.domain.cart.CartDto;
import com.example.jshop.carts_and_orders.domain.cart.CartItemsDto;
import com.example.jshop.customer.domain.LoggedCustomerDto;
import com.example.jshop.error_handlers.exceptions.*;
import com.example.jshop.carts_and_orders.mapper.CartMapper;
import com.example.jshop.carts_and_orders.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CartMapper cartMapper;

    @PostMapping
    ResponseEntity<CartDto> createCart() {
        return ResponseEntity.ok(cartMapper.mapCartToCartDto(cartService.createCart()));
    }

    @PutMapping("add")
    ResponseEntity<CartDto> addToCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException, NotEnoughItemsException, ItemNotAvailableException, ProductNotFoundException, InvalidQuantityException {
        return ResponseEntity.ok(cartService.addToCart(cartId, cartItemsDto));
    }

    @GetMapping
    ResponseEntity<CartDto> showCart(Long cartId) throws CartNotFoundException {
        return ResponseEntity.ok(cartMapper.mapCartToCartDto(cartService.showCart(cartId)));
    }

    @PutMapping("remove")
    ResponseEntity<CartDto> removeFromCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException, ProductNotFoundException {
        return ResponseEntity.ok(cartService.removeFromCart(cartId, cartItemsDto));
    }

    @DeleteMapping
    ResponseEntity<Void> cancelCart(@RequestParam Long cartId) throws CartNotFoundException {
        cartService.cancelCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("finalize/login")
    ResponseEntity<OrderDtoToCustomer> finalizeCart(@RequestParam Long cartId, @RequestBody LoggedCustomerDto loggedCustomerDto) throws CartNotFoundException, UserNotFoundException, AccessDeniedException {
        return ResponseEntity.ok(cartService.finalizeCart(cartId, loggedCustomerDto));
    }

    @PutMapping("pay/login")
    ResponseEntity<OrderDtoToCustomer> payForCartLogged(@RequestParam Long orderId, @RequestBody LoggedCustomerDto loggedCustomerDto) throws UserNotFoundException, AccessDeniedException, OrderNotFoundException, PaymentErrorException {
        return ResponseEntity.ok(cartService.payForCart(orderId, loggedCustomerDto));
    }

    @PutMapping("pay/unloged")
    ResponseEntity<OrderDtoToCustomer> payForCartUnLogged(@RequestParam Long cartId, @RequestBody UnlogedCustomerDto unlogedCustomerDto) throws PaymentErrorException, CartNotFoundException, UserNotFoundException, AccessDeniedException {
        cartService.payForCartUnlogged(cartId, unlogedCustomerDto);
        return ResponseEntity.ok(cartService.payForCartUnlogged(cartId, unlogedCustomerDto));
    }
}

/*

}

@Data
@ToString(exclude = { "password" })
public class AuthenticationRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private char[] password;
}*/
