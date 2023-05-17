package com.example.jshop.cartsandorders.controller;

import com.example.jshop.camunda.StartProcessShopping;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.cart.CartDto;
import com.example.jshop.cartsandorders.domain.cart.CartItemsDto;
import com.example.jshop.cartsandorders.domain.order.OrderIdDTO;
import com.example.jshop.cartsandorders.mapper.CartMapper;
import com.example.jshop.cartsandorders.service.CartService;
import com.example.jshop.customer.domain.UnauthenticatedCustomerDto;
import com.example.jshop.errorhandlers.exceptions.CartNotFoundException;
import com.example.jshop.errorhandlers.exceptions.InvalidCustomerDataException;
import com.example.jshop.errorhandlers.exceptions.InvalidDataException;
import com.example.jshop.errorhandlers.exceptions.InvalidQuantityException;
import com.example.jshop.errorhandlers.exceptions.NotEnoughItemsException;
import com.example.jshop.errorhandlers.exceptions.OrderNotFoundException;
import com.example.jshop.errorhandlers.exceptions.PaymentErrorException;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.errorhandlers.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ResponseEntity<CartDto> addToCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto)
        throws InvalidQuantityException, NotEnoughItemsException, ProductNotFoundException, CartNotFoundException {
        return ResponseEntity.ok(cartService.addToCart(cartId, cartItemsDto));
    }

    @GetMapping
    ResponseEntity<CartDto> showCart(@RequestParam Long cartId) throws CartNotFoundException {
        Cart cart = cartService.showCart(cartId);
        return ResponseEntity.ok(cartMapper.mapCartToCartDto(cart));
    }

    @PutMapping("remove")
    ResponseEntity<CartDto> removeFromCart(@RequestParam Long cartId, @RequestBody CartItemsDto cartItemsDto)
        throws InvalidQuantityException, CartNotFoundException, ProductNotFoundException {
        cartService.removeFromCart(cartId, cartItemsDto);
        return ResponseEntity.ok(cartService.removeFromCart(cartId, cartItemsDto));
    }

    @PostMapping("finalize")
    ResponseEntity<Void> decideToFinalizeCart(@RequestParam Long cartId, @RequestParam String authenticated)
        throws CartNotFoundException, InvalidDataException {
        cartService.decideToFinalize(cartId, authenticated);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("finalize/login")
    ResponseEntity<OrderIdDTO> finalizeCart(@RequestParam Long cartId, Authentication auth)
        throws CartNotFoundException, UserNotFoundException, InvalidCustomerDataException {
        String userName = (String) auth.getPrincipal();
        return ResponseEntity.ok(cartService.finalizeCart(cartId, userName));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("pay/login")
    ResponseEntity<Void> payForOrderLogged(@RequestParam Long orderId, Authentication auth)
        throws OrderNotFoundException, PaymentErrorException {
        String userName = (String) auth.getPrincipal();
        cartService.payForOrder(orderId, userName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("pay/unauthenticated")
    ResponseEntity<Void> payForCartUnauthenticated(@RequestParam Long cartId,
        @RequestBody UnauthenticatedCustomerDto unauthenticatedCustomerDto)
        throws InvalidCustomerDataException, CartNotFoundException {
        cartService.payForCartUnauthenticatedCustomer(cartId, unauthenticatedCustomerDto);
        return ResponseEntity.ok().build();
    }
}

