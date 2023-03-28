package com.example.jshop.cartsandorders.controller;

import com.example.jshop.camunda.StartProcessShopping;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.errorhandlers.exceptions.InvalidCustomerDataException;
import com.example.jshop.customer.domain.AuthenticationDataDto;
import com.example.jshop.customer.domain.UnauthenticatedCustomerDto;
import com.example.jshop.cartsandorders.domain.order.OrderDtoToCustomer;
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
    ResponseEntity<CartDto> addToCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException, NotEnoughItemsException, ProductNotFoundException, InvalidQuantityException {
        return ResponseEntity.ok(cartService.addToCart(cartId, cartItemsDto));
    }

    @GetMapping
    ResponseEntity<CartDto> showCart(@RequestParam Long cartId) throws CartNotFoundException {
        Cart cart = cartService.showCart(cartId);
        return ResponseEntity.ok(cartMapper.mapCartToCartDto(cart));
    }

    @PutMapping("remove")
    ResponseEntity<CartDto> removeFromCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto) throws CartNotFoundException, ProductNotFoundException, InvalidQuantityException {
        return ResponseEntity.ok(cartService.removeFromCart(cartId, cartItemsDto));
    }

    @DeleteMapping
    ResponseEntity<Void> cancelCart(@RequestParam Long cartId) throws CartNotFoundException, ProductNotFoundException {
        cartService.cancelCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("finalize/login")
    ResponseEntity<OrderDtoToCustomer> finalizeCart(@RequestParam Long cartId, @RequestBody AuthenticationDataDto authenticationDataDto) throws CartNotFoundException, UserNotFoundException, AccessDeniedException, InvalidCustomerDataException {
        return ResponseEntity.ok(cartService.finalizeCart(cartId, authenticationDataDto));
    }

    @PutMapping("pay/login")
    ResponseEntity<OrderDtoToCustomer> payForCartLogged(@RequestParam Long orderId, @RequestBody AuthenticationDataDto authenticationDataDto) throws UserNotFoundException, AccessDeniedException, OrderNotFoundException, PaymentErrorException, InvalidCustomerDataException {
        return ResponseEntity.ok(cartService.payForCart(orderId, authenticationDataDto));
    }

    @PutMapping("pay/unauthenticated")
    ResponseEntity<OrderDtoToCustomer> payForCartUnauthenticated(@RequestParam Long cartId, @RequestBody UnauthenticatedCustomerDto unauthenticatedCustomerDto) throws PaymentErrorException, CartNotFoundException, InvalidCustomerDataException {
        return ResponseEntity.ok(cartService.payForCartUnauthenticatedCustomer(cartId, unauthenticatedCustomerDto));
    }
}

