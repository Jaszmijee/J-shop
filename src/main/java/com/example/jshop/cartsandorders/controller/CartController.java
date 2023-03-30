package com.example.jshop.cartsandorders.controller;

import com.example.jshop.camunda.StartProcessShopping;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.errorhandlers.exceptions.InvalidCustomerDataException;
import com.example.jshop.customer.domain.AuthenticationDataDto;
import com.example.jshop.customer.domain.UnauthenticatedCustomerDto;
import com.example.jshop.cartsandorders.domain.cart.CartDto;
import com.example.jshop.cartsandorders.domain.cart.CartItemsDto;
import com.example.jshop.errorhandlers.exceptions.*;
import com.example.jshop.cartsandorders.mapper.CartMapper;
import com.example.jshop.cartsandorders.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/j-shop/cart")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final StartProcessShopping processShopping;


    @PostMapping
    ResponseEntity<CartDto> createCart() throws CartNotFoundException {
        Cart cart = cartService.createCart();
        processShopping.createProcessInstance(cart.getCartID());
        return ResponseEntity.ok(cartMapper.mapCartToCartDto(cart));
    }

    @PutMapping("add")
    ResponseEntity<Void> addToCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws InvalidQuantityException {
        cartService.addToCart(cartId, cartItemsDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<CartDto> showCart(@RequestParam Long cartId) throws CartNotFoundException {
        Cart cart = cartService.showCart(cartId);
        return ResponseEntity.ok(cartMapper.mapCartToCartDto(cart));
    }

    @PutMapping("remove")
    ResponseEntity<Void> removeFromCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws InvalidQuantityException {
        cartService.removeFromCart(cartId, cartItemsDto);
    return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> cancelCart(@RequestParam Long cartId) throws CartNotFoundException {
        cartService.cancelCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("finalize")
    ResponseEntity<Void> decideToFinalizeCart(@RequestParam Long cartId, @RequestParam String authenticated) throws InvalidArgumentException {
        cartService.decideToFinalize(cartId, authenticated);
        return ResponseEntity.ok().build();
    }

    @PutMapping("finalize/login")
    ResponseEntity<Void> finalizeCart(@RequestParam Long cartId, @RequestBody AuthenticationDataDto authenticationDataDto) throws UserNotFoundException, AccessDeniedException, InvalidCustomerDataException {
        cartService.finalizeCart(cartId, authenticationDataDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("pay/login")
    ResponseEntity<Void> payForOrderLogged(@RequestParam Long orderId, @RequestBody AuthenticationDataDto authenticationDataDto) throws OrderNotFoundException {
        cartService.payForOrder(orderId, authenticationDataDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("pay/unauthenticated")
    ResponseEntity<Void> payForCartUnauthenticated(@RequestParam Long cartId, @RequestBody UnauthenticatedCustomerDto unauthenticatedCustomerDto) throws InvalidCustomerDataException {
        cartService.payForCartUnauthenticatedCustomer(cartId, unauthenticatedCustomerDto);
    return ResponseEntity.ok().build();
    }
}

