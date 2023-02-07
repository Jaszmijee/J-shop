package com.example.jshop.controller;

import com.example.jshop.domain.cart.CartDto;
import com.example.jshop.domain.cart.CartItemsDto;
import com.example.jshop.exception.CartNotFoundException;
import com.example.jshop.exception.ItemNotAvailableException;
import com.example.jshop.exception.NotEnoughItemsException;
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
    ResponseEntity<Void> createCart(){
        cartService.createCart();
        return ResponseEntity.ok().build();
    }

/*    @PutMapping
    ResponseEntity<CartDto> addToCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException, NotEnoughItemsException, ItemNotAvailableException {
        cartService.addToCart(cartId, cartItemsDto);
        return ResponseEntity.ok(cartMapper.mapToCartDto);
  }*/

/*    @PutMapping
    ResponseEntity<CartDto> removeFromCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException {
        cartService.removeFromCart(cartId, cartItemsDto);
        return ResponseEntity.ok(cartMapper.mapToCartDto);
    }

    @DeleteMapping
    ResponseEntity<Void> cancelCart(@RequestParam Long cartId) throws CartNotFoundException {
        cartService.removeCart(cartId);
        return ResponseEntity.ok().build();
    }*/

/*    @PutMapping
    ResponseEntity<Void> finalizeCart(@RequestParam Long cartId, @RequestBody CustomerDto customerDto) {
        cartService.finalizeCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    ResponseEntity<Void> payForCart(@RequestParam Long cartId, @RequestBody UserDto userDto) {
        cartService.payForCart(cartId);
        return ResponseEntity.ok().build();
    }*/
}
