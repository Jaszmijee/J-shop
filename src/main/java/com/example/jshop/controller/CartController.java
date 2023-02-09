package com.example.jshop.controller;

import com.example.jshop.domain.order.OrderDtoToCustomer;
import com.example.jshop.domain.cart.CartDto;
import com.example.jshop.domain.cart.CartItemsDto;
import com.example.jshop.domain.customer.LoggedCustomerDto;
import com.example.jshop.exception.*;
import com.example.jshop.mapper.CartMapper;
import com.example.jshop.service.CartService;
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
    ResponseEntity<CartDto> addToCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException, NotEnoughItemsException, ItemNotAvailableException, ProductNotFoundException {
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
        cartService.removeCart(cartId);
        return ResponseEntity.ok().build();
    }


  @PutMapping("finalize/login")
    ResponseEntity<OrderDtoToCustomer> finalizeCart(@RequestParam Long cartId, @RequestBody LoggedCustomerDto loggedCustomerDto) throws CartNotFoundException, UserNotFoundException, AccessDeniedException {
        cartService.finalizeCart(cartId, loggedCustomerDto);
        return ResponseEntity.ok().build();
    }

 /*   @PutMapping
    ResponseEntity<Void> payForCart(@RequestParam Long cartId, @RequestBody String username UserDto userDto) {
        cartService.payForCart(cartId);
        return ResponseEntity.ok().build();
    }*/
}

/*
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) throws GeneralSecurityException {
        return mapper.map(authenticationService.login(request.getUsername(), request.getPassword()), AuthenticationResponse.class);
    }
}

@Data
@ToString(exclude = { "password" })
public class AuthenticationRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private char[] password;
}*/
